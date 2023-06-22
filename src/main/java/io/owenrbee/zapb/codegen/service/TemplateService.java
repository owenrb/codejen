package io.owenrbee.zapb.codegen.service;

import java.io.StringWriter;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.stereotype.Service;

import io.owenrbee.zapb.codegen.model.param.PomVO;
import io.owenrbee.zapb.codegen.model.spec.FileVO;

@Service
public class TemplateService {

    public FileVO generatePom(PomVO vo) {

        Template t = getTemplate("src/main/resources/templates/pom.vm");
        VelocityContext context = new VelocityContext();
        context.put("maven", vo);

        StringWriter writer = new StringWriter();
        t.merge(context, writer);

        return new FileVO("", "pom.xml", writer.toString());

    }

    public FileVO generateMain(String packageName) {

        Template t = getTemplate("src/main/resources/templates/java/MainApplication.vm");
        VelocityContext context = new VelocityContext();
        context.put("packageName", packageName);

        StringWriter writer = new StringWriter();
        t.merge(context, writer);

        return new FileVO("src/main/java/" + packageName.replaceAll("\\.", "/"), "MainApplication.java",
                writer.toString());

    }

    public FileVO[] generateExceptions(String packageName) {

        Template t1 = getTemplate("src/main/resources/templates/java/common/exception/DaoException.vm");
        VelocityContext context1 = new VelocityContext();
        context1.put("packageName", packageName);

        StringWriter writer1 = new StringWriter();
        t1.merge(context1, writer1);

        FileVO daoEx = new FileVO("src/main/java/" + packageName.replaceAll("\\.", "/") + "/common/exception",
                "DaoException.java",
                writer1.toString());

        Template t2 = getTemplate("src/main/resources/templates/java/common/exception/ServiceException.vm");
        VelocityContext context2 = new VelocityContext();
        context2.put("packageName", packageName);

        StringWriter writer2 = new StringWriter();
        t2.merge(context1, writer1);

        FileVO serviceEx = new FileVO("src/main/java/" + packageName.replaceAll("\\.", "/") + "/common/exception",
                "ServiceException.java",
                writer2.toString());

        return new FileVO[] { daoEx, serviceEx };

    }

    public FileVO generateIDao(String packageName) {

        Template t = getTemplate("src/main/resources/templates/java/common/dao/IDao.vm");
        VelocityContext context = new VelocityContext();
        context.put("packageName", packageName);

        StringWriter writer = new StringWriter();
        t.merge(context, writer);

        return new FileVO("src/main/java/" + packageName.replaceAll("\\.", "/") + "/common/dao",
                "IDao.java",
                writer.toString());
    }

    private Template getTemplate(String template) {

        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.init();

        return velocityEngine.getTemplate(template);
    }

}
