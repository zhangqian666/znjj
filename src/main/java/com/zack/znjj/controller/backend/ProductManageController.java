package com.zack.znjj.controller.backend;

import com.google.common.collect.Maps;

import com.zack.znjj.common.restful.Const;
import com.zack.znjj.common.restful.ResponseCode;
import com.zack.znjj.common.restful.ServerResponse;
import com.zack.znjj.model.Product;
import com.zack.znjj.model.User;
import com.zack.znjj.service.IFileService;
import com.zack.znjj.service.IProductService;
import com.zack.znjj.service.IUserService;
import com.zack.znjj.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by geely
 */
@Slf4j
@RestController
@RequestMapping("/manage/product/")
public class ProductManageController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IProductService iProductService;
    @Autowired
    private IFileService iFileService;

    @RequestMapping("save/")
    public ServerResponse productSave(Product product, HttpServletRequest httpServletRequest) {
        ServerResponse<User> userServerResponse = iUserService.parseRequest(httpServletRequest);
        if (userServerResponse.isSuccess()) {
            if (iUserService.checkAdminRole(userServerResponse.getData()).isSuccess()) {
                //填充我们增加产品的业务逻辑
                return iProductService.saveOrUpdateProduct(product);
            } else {
                return ServerResponse.createByErrorMessage("无权限操作");
            }
        }
        return userServerResponse;
    }

    @RequestMapping("set/sale/status/")
    public ServerResponse setSaleStatus(Integer productId, Integer status, HttpServletRequest httpServletRequest) {
        ServerResponse<User> userServerResponse = iUserService.parseRequest(httpServletRequest);
        if (userServerResponse.isSuccess()) {
            if (iUserService.checkAdminRole(userServerResponse.getData()).isSuccess()) {
                return iProductService.setSaleStatus(productId, status);
            } else {
                return ServerResponse.createByErrorMessage("无权限操作");
            }
        }
        return userServerResponse;
    }

    @RequestMapping("detail/")
    public ServerResponse getDetail(HttpServletRequest httpServletRequest, Integer productId) {
        ServerResponse<User> userServerResponse = iUserService.parseRequest(httpServletRequest);
        if (userServerResponse.isSuccess()) {
            if (iUserService.checkAdminRole(userServerResponse.getData()).isSuccess()) {
                //填充业务
                return iProductService.manageProductDetail(productId);
            } else {
                return ServerResponse.createByErrorMessage("无权限操作");
            }
        }
        return userServerResponse;
    }

    @RequestMapping("list/")
    public ServerResponse getList(HttpServletRequest httpServletRequest, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        ServerResponse<User> userServerResponse = iUserService.parseRequest(httpServletRequest);
        if (userServerResponse.isSuccess()) {
            if (iUserService.checkAdminRole(userServerResponse.getData()).isSuccess()) {
                //填充业务
                return iProductService.getProductList(pageNum, pageSize);
            } else {
                return ServerResponse.createByErrorMessage("无权限操作");
            }
        }
        return userServerResponse;
    }

    @RequestMapping("search/")
    public ServerResponse productSearch(HttpServletRequest httpServletRequest, String productName, Integer productId, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        ServerResponse<User> userServerResponse = iUserService.parseRequest(httpServletRequest);
        if (userServerResponse.isSuccess()) {
            if (iUserService.checkAdminRole(userServerResponse.getData()).isSuccess()) {
                //填充业务
                return iProductService.searchProduct(productName, productId, pageNum, pageSize);
            } else {
                return ServerResponse.createByErrorMessage("无权限操作");
            }
        }
        return userServerResponse;
    }

    @RequestMapping("upload/")
    public ServerResponse upload(@RequestParam(value = "upload_file", required = false) MultipartFile file, HttpServletRequest httpServletRequest) {
        ServerResponse<User> userServerResponse = iUserService.parseRequest(httpServletRequest);
        if (userServerResponse.isSuccess()) {
            if (iUserService.checkAdminRole(userServerResponse.getData()).isSuccess()) {
                String path = httpServletRequest.getSession().getServletContext().getRealPath("upload");
                String targetFileName = iFileService.upload(file, path);
                String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;
                Map fileMap = Maps.newHashMap();
                fileMap.put("uri", targetFileName);
                fileMap.put("url", url);
                return ServerResponse.createBySuccess(fileMap);
            } else {
                return ServerResponse.createByErrorMessage("无权限操作");
            }
        }

        return userServerResponse;

    }


    @RequestMapping("richtext/img/upload/")
    public Map richtextImgUpload(HttpServletRequest httpServletRequest, @RequestParam(value = "upload_file", required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        Map resultMap = Maps.newHashMap();
        ServerResponse<User> userServerResponse = iUserService.parseRequest(httpServletRequest);
        if (userServerResponse.isSuccess()) {
            if (iUserService.checkAdminRole(userServerResponse.getData()).isSuccess()) {
                log.error("开始上传");
                String path = request.getSession().getServletContext().getRealPath("upload");
                String targetFileName = iFileService.upload(file, path);
                if (StringUtils.isBlank(targetFileName)) {
                    resultMap.put("success", false);
                    resultMap.put("msg", "上传失败");
                    return resultMap;
                }
                String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;
                resultMap.put("success", true);
                resultMap.put("msg", "上传成功");
                resultMap.put("file_path", url);
                response.addHeader("Access-Control-Allow-Headers", "X-File-Name");
                return resultMap;
            } else {
                resultMap.put("success", false);
                resultMap.put("msg", "无权限操作");
                return resultMap;
            }
        } else {
            resultMap.put("success", false);
            resultMap.put("msg", "请登录管理员");
            return resultMap;
        }
    }


}
