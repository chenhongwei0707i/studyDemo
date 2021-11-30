package com.chw.project_pro_max.controller;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.XmlUtil;
import cn.hutool.http.webservice.SoapClient;
import cn.hutool.json.JSONObject;
import com.sun.org.apache.regexp.internal.RE;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.dom4j.Attribute;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.json.XML;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Document;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.sound.midi.Soundbank;
import javax.xml.parsers.SAXParser;
import javax.xml.xpath.XPathConstants;
import java.util.Iterator;
import java.util.List;

@RestController

@EnableSwagger2
@Api( tags = "调用第三方接口")
public class XMLController {

    @GetMapping("/getPracCertList")
    @ApiOperation("从业人员列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "idCertNum", value = "身份证")})
    @ResponseBody
    public JSONObject getPracCertList(@RequestParam String idCertNum) {
        JSONObject jsonObject = new JSONObject();
        SoapClient client = null;Object getDataResult = null;
        try {
            client = SoapClient.create("http://www.tol.org.cn/jstmbs_api/WebService/DataInterfaceService.asmx")
                    .setMethod("GetData", "")
                    .setParam("funName", "GetPracCertList")
                    .setCharset(CharsetUtil.CHARSET_UTF_8)
                    .setParam("userId", "dzzztest")
                    .setParam("password", "12345678")
                    .setParam("param", "<jstmbsinterface>\n" +
                            "<condition>\n" +
                            "<field name=\"idCertNum\" value=\""+idCertNum+"\"></field>\n" +
                            "</condition>\n" +
                            "</jstmbsinterface>\n");

            String send = client.send(true);
            Document docResult = XmlUtil.readXML(send);
            getDataResult = XmlUtil.getByXPath("//GetDataResponse/GetDataResult", docResult, XPathConstants.STRING);
            Document document = XmlUtil.parseXml(String.valueOf(getDataResult));
            SAXReader read = new SAXReader();
            try {
                org.dom4j.Document document1 = DocumentHelper.parseText(String.valueOf(getDataResult));
                Element rootElement = document1.getRootElement();
                List list = listNodes(rootElement);
                System.out.println(list);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.putOpt("code", 1);
        }

        // org.json.JSONObject jsonObject = XML.toJSONObject(String.valueOf(getDataResult));



        return jsonObject;
    }


    @GetMapping("/test/{data}")
    @ApiOperation("test")
    public String get(@PathVariable("data") String data ) {
        System.out.println("testdata = " + data);
        return data;
    }

    @GetMapping("/gkdecrypt")
    @ApiOperation("港口AES解密")

    public void gkdecrypt(@RequestParam("data") String data) {
        System.out.println("12321==="+data);
    }

    public List listNodes(Element node) {
        System.out.println("当前节点的名称: :" + node.getName());
        //获取当前节点的所有属性节点
        List<Attribute> list = node.attributes();
        //遍历属性节点
        for (Attribute attr : list) {
            System.out.println(attr.getText() + "-----" + attr.getName() + "====" + attr.getValue());
        }
        if (!(node.getTextTrim().equals(""))) {
            System.out.println("文本内容: : : :" + node.getText());
        }
        //当前节点下面子节点迭代器
        Iterator<Element> it = node.elementIterator();
        //遍历
        while (it.hasNext()) {
            //获取某个子节点对象
            Element e = it.next();
            //对子节点进行遍历
            listNodes(e);
        }
        return list;
    }
}
