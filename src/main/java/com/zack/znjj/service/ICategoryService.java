package com.zack.znjj.service;


import com.zack.znjj.common.restful.ServerResponse;
import com.zack.znjj.model.Category;

import java.util.List;

/**
 * Created by geely
 */
public interface ICategoryService {
    ServerResponse addCategory(String categoryName, Integer parentId);
    ServerResponse updateCategoryName(Integer categoryId, String categoryName);
    ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId);
    ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId);

}
