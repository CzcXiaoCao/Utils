package com.zhujie.complies;

/**
 * Created by CaoZhiChao on 2018/11/5 16:11
 */

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.zhujie.annotions.BindView;
import com.zhujie.annotions.OnClick;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * Created by will on 2018/2/4.
 */

@AutoService(Processor.class)
//@SupportedAnnotationTypes({"com.zhujie.annotions.BindView"})
//@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class BindViewProcessor extends AbstractProcessor {
    /**
     * 工具类,可以从init方法的ProcessingEnvironment中获取
     */
    private Elements elementUtils;
    private Messager mMessager;
    private Filer mFileUtils;
    /**
     * 缓存所有子Element
     * key:父Element类名
     * value:子Element
     */
    private HashMap<String, List<Element>> cacheElements_BindView = null;
    private HashMap<String, List<Element>> cacheElements_OnClick = null;
    /**
     * 保存已经OnClick修饰的id，在BindView中就OnClick了
     */
    private List<Integer> idOnClickAnnotion = new ArrayList<>();
    /**
     * 缓存所有父Element
     * key:父Element类名
     * value:父Element
     */
    private HashMap<String, Element> cacheAllParentElements = null;

    @Override
    public Set<String> getSupportedAnnotationTypes() {
//        Set<String> annotation = new LinkedHashSet<>();
//        annotation.add(BindView.class.getCanonicalName());
        // 规定需要处理的注解类型
        return Collections.singleton(BindView.class.getCanonicalName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations
            , RoundEnvironment roundEnv) {
        //控制台打印数据 非常重要的是Kind.ERROR
        Log.e(mMessager, "开始处理进程Processor");
        //扫描所有注解了BindView的Field,因为我们所有注解BindView的地方都是一个Activity的成员
        //返回所有被注解了@BindView的元素的列表。Element可以是类、方法、变量等
        Set<? extends Element> elements_BindView = roundEnv.getElementsAnnotatedWith(BindView.class);
        Set<? extends Element> elements_OnClick = roundEnv.getElementsAnnotatedWith(OnClick.class);
        for (Element element : elements_BindView) {
            //将所有子elements进行过滤
            addElementToCache(element);
        }
        for (Element element : elements_OnClick) {
            //将所有子elements进行过滤
            addElementToCacheClick(element);
        }

        if (cacheElements_BindView == null || cacheElements_BindView.size() == 0) {
            return true;
        }
        for (String parentElementName : cacheElements_BindView.keySet()) {
            //判断一下获取到的parent element是否是类
            try {
                ParameterSpec savedInstanceState = ParameterSpec.builder(ClassName.get(cacheAllParentElements.get(parentElementName).asType()), "targetActivity")
                        .addModifiers(Modifier.FINAL)
                        .build();

                //使用JavaPoet构造一个方法
                MethodSpec.Builder bindViewMethodSpec = MethodSpec.methodBuilder("bindView")
                        .addModifiers(Modifier.PUBLIC)
                        .returns(void.class)
                        .addParameter(savedInstanceState);

                List<Element> childElements = cacheElements_BindView.get(parentElementName);
                List<Element> childElements_OnClick = cacheElements_OnClick.get(parentElementName);
                //这个方法要在{@Link #buildBindView()}
                /**
                 * {@link #buildBindView(List, MethodSpec.Builder)}
                 */
                buildOnClick(childElements_OnClick, bindViewMethodSpec);
                buildBindView(childElements, bindViewMethodSpec);
                //构造一个类,以Bind_开头
                TypeSpec typeElement = TypeSpec.classBuilder("Bind_" + cacheAllParentElements.get(parentElementName).getSimpleName())
                        .addModifiers(Modifier.PUBLIC)
                        .addMethod(bindViewMethodSpec.build())
                        .build();
                //进行文件写入
                JavaFile javaFile = JavaFile.builder(
                        getPackageName((TypeElement) cacheAllParentElements.get(parentElementName))
                        , typeElement)
                        .build();
                javaFile.writeTo(processingEnv.getFiler());
            } catch (IOException e) {
                e.printStackTrace();
                return true;
            }
        }
        return true;
    }

    private void buildOnClick(List<Element> childElements_OnClick, MethodSpec.Builder builder) {
        if (childElements_OnClick != null && childElements_OnClick.size() != 0) {
            for (Element childElement : childElements_OnClick) {
                OnClick bindView = childElement.getAnnotation(OnClick.class);
                int[] ids = bindView.value();
                for (int id : ids) {
                    checkId(id, childElement);
                    idOnClickAnnotion.add(id);
                    builder.addStatement("targetActivity.findViewById($L).setOnClickListener( $L)", id, getOnClickTypeSpec(childElement));
                }
            }
        }
    }

    private void buildBindView(List<Element> childElements, MethodSpec.Builder builder) {
        if (childElements != null && childElements.size() != 0) {
            for (Element childElement : childElements) {
                Log.e(mMessager, "childElements名称：     " + childElement.getSimpleName());
                BindView bindView = childElement.getAnnotation(BindView.class);
                int id = bindView.id();
                if (id <= 0) {
                    throw new IllegalArgumentException(
                            String.format("id() in @%s for class %s is null or empty! that's not allowed",
                                    BindView.class.getSimpleName(), childElement.getSimpleName()));
                }
                //使用JavaPoet对方法内容进行添加
                builder.addStatement(
                        String.format("targetActivity.%s = (%s) targetActivity.findViewById(%s)"
                                , childElement.getSimpleName()
                                , ClassName.get(childElement.asType()).toString()
                                , bindView.id()));
                if (bindView.isClick() && !idOnClickAnnotion.contains(id)) {
                    builder.addStatement(
                            String.format("targetActivity.%s.setOnClickListener((android.view.View.OnClickListener)targetActivity)"
                                    , childElement.getSimpleName()));
                }
            }
        }
    }

    private TypeSpec getOnClickTypeSpec(Element childElement) {
        ClassName clickListener = ClassName.get("android.view.View", "OnClickListener");
        ClassName viewClass = ClassName.bestGuess("android.view.View");
        TypeSpec onclick = TypeSpec.anonymousClassBuilder("")
                .addSuperinterface(clickListener)
                .addMethod(MethodSpec.methodBuilder("onClick")
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(viewClass, "v")
                        .addStatement(String.format("targetActivity.%s()", childElement.getSimpleName()))
                        .addStatement(String.format("android.util.Log.e(%s,%s)"
                                , "\"" + 1234 + "\"", "\"" + childElement.getSimpleName() + "     is Clicked" + "\""))
                        .returns(void.class)
                        .build())
                .build();
        return onclick;
    }

    private void checkId(int id, Element childElement) {
        if (id <= 0) {
            throw new IllegalArgumentException(
                    String.format("id() in @%s for class %s is null or empty! that's not allowed",
                            OnClick.class.getSimpleName(), childElement.getSimpleName()));
        }
    }

    private static MethodSpec whatsMyName(String name) {
        return MethodSpec.methodBuilder(name)
                .returns(String.class)
                .addStatement("return $S", name)
                .build();
    }

    private String getPackageName(TypeElement type) {
        return elementUtils.getPackageOf(type).getQualifiedName().toString();
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        //返回实现Messager接口的对象，用于输出控制台信息
        mMessager = processingEnv.getMessager();
        //返回实现Filer接口的对象，用于创建文件、类和辅助文件。
        mFileUtils = processingEnv.getFiler();
        //用于元素处理
        elementUtils = processingEnv.getElementUtils();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    /**
     * 缓存父Element对应的所有子Element
     * 缓存父Element
     * Element代表的是源代码。TypeElement代表的是源代码中的类型元素，例如类。
     * 然而，TypeElement并不包含类本身的信息。
     * 你可以从TypeElement中获取类的名字，但是你获取不到类的信息，例如它的父类。
     * 这种信息需要通过TypeMirror获取。你可以通过调用elements.asType()获取元素的TypeMirror
     *
     * @param childElement
     */
    private void addElementToCache(Element childElement) {
        Log.e(mMessager, "开始处理进程Processor" + childElement.getKind() + "      " + childElement.getKind().isField());
        if (!childElement.getKind().isField()) {
            error(childElement, "喂：Only Field can be annotated with @%s",
                    BindView.class.getSimpleName());
        }
        if (cacheElements_BindView == null) {
            cacheElements_BindView = new HashMap<>();
        }

        if (cacheAllParentElements == null) {
            cacheAllParentElements = new HashMap<>();
        }
        //父Element类名
        String parentElementName = null;
        //2.获取包装类（父类）类型：com.zhujie.zhujie.MainActivity
        TypeElement enclosingElement = (TypeElement) childElement.getEnclosingElement();
//        String enclosingName = enclosingElement.getQualifiedName().toString();
        parentElementName = ClassName.get(enclosingElement.asType()).toString();
        Log.e(mMessager, "哈哈：        " + parentElementName + "        " + childElement.getSimpleName());
        if (cacheElements_BindView.containsKey(parentElementName)) {
            List<Element> childElements = cacheElements_BindView.get(parentElementName);
            childElements.add(childElement);
        } else {
            ArrayList<Element> childElements = new ArrayList<>();
            childElements.add(childElement);
            cacheElements_BindView.put(parentElementName, childElements);
            cacheAllParentElements.put(parentElementName, childElement.getEnclosingElement());
        }
    }

    private void addElementToCacheClick(Element childElement) {
        Log.e(mMessager, "开始处理进程Processor" + childElement.getKind() + "      " + childElement.getKind().isField());
        if (!childElement.getKind().equals(ElementKind.METHOD)) {
            error(childElement, "哈：Only Method can be annotated with @%s",
                    BindView.class.getSimpleName());
        }
        if (cacheElements_OnClick == null) {
            cacheElements_OnClick = new HashMap<>();
        }

        if (cacheAllParentElements == null) {
            cacheAllParentElements = new HashMap<>();
        }
        //父Element类名
        String parentElementName = null;
        //2.获取包装类（父类）类型：com.zhujie.zhujie.MainActivity
        TypeElement enclosingElement = (TypeElement) childElement.getEnclosingElement();
//        String enclosingName = enclosingElement.getQualifiedName().toString();
        parentElementName = ClassName.get(enclosingElement.asType()).toString();
        Log.e(mMessager, "嘿嘿嘿：        " + parentElementName + "        " + childElement.getSimpleName());
        if (cacheElements_OnClick.containsKey(parentElementName)) {
            List<Element> childElements = cacheElements_OnClick.get(parentElementName);
            childElements.add(childElement);
        } else {
            ArrayList<Element> childElements = new ArrayList<>();
            childElements.add(childElement);
            cacheElements_OnClick.put(parentElementName, childElements);
            cacheAllParentElements.put(parentElementName, childElement.getEnclosingElement());
        }
    }

    private void error(Element e, String msg, Object... args) {
        mMessager.printMessage(
                Diagnostic.Kind.ERROR,
                String.format(msg, args),
                e);
    }
}
