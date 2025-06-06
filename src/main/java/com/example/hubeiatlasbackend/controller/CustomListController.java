package com.example.hubeiatlasbackend.controller;

import com.example.hubeiatlasbackend.service.CustomListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/customlist")
public class CustomListController extends BaseController {

    @Autowired
    private CustomListService customListService;

    /**
     * 获取用户的所有自定义列表
     */
    @GetMapping("/list")
    public Object getUserLists(@RequestParam("userId") String userIdStr) {
        try {
            UUID userId = UUID.fromString(userIdStr);
            List<Map<String, Object>> lists = customListService.getUserLists(userId);
            return renderSuccess(lists);
        } catch (IllegalArgumentException e) {
            return renderError("用户ID格式错误");
        } catch (Exception e) {
            return renderError(e.getMessage());
        }
    }

    /**
     * 创建自定义列表
     */
    @PostMapping("/create")
    public Object createList(@RequestParam("userId") String userIdStr,
                             @RequestParam("name") String name,
                             @RequestParam(value = "description", required = false, defaultValue = "") String description) {
        try {
            UUID userId = UUID.fromString(userIdStr);
            Map<String, Object> result = customListService.createList(userId, name, description);
            return renderSuccess("创建成功", result);
        } catch (IllegalArgumentException e) {
            return renderError("参数格式错误: " + e.getMessage());
        } catch (Exception e) {
            return renderError(e.getMessage());
        }
    }

    /**
     * 更新自定义列表
     */
    @PostMapping("/update")
    public Object updateList(@RequestParam("listId") String listIdStr,
                             @RequestParam("userId") String userIdStr,
                             @RequestParam("name") String name,
                             @RequestParam(value = "description", required = false, defaultValue = "") String description) {
        try {
            UUID listId = UUID.fromString(listIdStr);
            UUID userId = UUID.fromString(userIdStr);
            boolean success = customListService.updateList(listId, userId, name, description);

            if (success) {
                return renderSuccess("更新成功");
            } else {
                return renderError("更新失败，请检查列表ID和用户权限");
            }
        } catch (IllegalArgumentException e) {
            return renderError("参数格式错误: " + e.getMessage());
        } catch (Exception e) {
            return renderError(e.getMessage());
        }
    }

    /**
     * 删除自定义列表
     */
    @PostMapping("/delete")
    public Object deleteList(@RequestParam("listId") String listIdStr,
                             @RequestParam("userId") String userIdStr) {
        try {
            UUID listId = UUID.fromString(listIdStr);
            UUID userId = UUID.fromString(userIdStr);
            boolean success = customListService.deleteList(listId, userId);

            if (success) {
                return renderSuccess("删除成功");
            } else {
                return renderError("删除失败，请检查列表ID和用户权限");
            }
        } catch (IllegalArgumentException e) {
            return renderError("参数格式错误: " + e.getMessage());
        } catch (Exception e) {
            return renderError(e.getMessage());
        }
    }

    /**
     * 获取自定义列表详情
     */
    @GetMapping("/detail/{listId}")
    public Object getListDetail(@PathVariable("listId") String listIdStr) {
        try {
            UUID listId = UUID.fromString(listIdStr);
            Map<String, Object> listDetail = customListService.getListDetail(listId);

            if (listDetail == null) {
                return renderError("列表不存在");
            }
            return renderSuccess(listDetail);
        } catch (IllegalArgumentException e) {
            return renderError("列表ID格式错误");
        } catch (Exception e) {
            return renderError(e.getMessage());
        }
    }

    /**
     * 检查地图是否在列表中
     */
    @GetMapping("/checkMap")
    public Object checkMapInList(@RequestParam("listId") String listIdStr,
                                 @RequestParam("mapId") String mapIdStr) {
        try {
            UUID listId = UUID.fromString(listIdStr);
            UUID mapId = UUID.fromString(mapIdStr);
            boolean inList = customListService.isMapInList(listId, mapId);
            return renderSuccess(Map.of("inList", inList));
        } catch (IllegalArgumentException e) {
            return renderError("参数格式错误");
        } catch (Exception e) {
            return renderError(e.getMessage());
        }
    }

    /**
     * 添加地图到列表
     */
    @PostMapping("/addMap")
    public Object addMapToList(@RequestParam("listId") String listIdStr,
                               @RequestParam("mapId") String mapIdStr,
                               @RequestParam("userId") String userIdStr) {
        try {
            UUID listId = UUID.fromString(listIdStr);
            UUID mapId = UUID.fromString(mapIdStr);
            UUID userId = UUID.fromString(userIdStr);

            boolean success = customListService.addMapToList(listId, mapId, userId);
            if (success) {
                return renderSuccess("添加成功");
            } else {
                return renderError("添加失败，请检查列表权限");
            }
        } catch (IllegalArgumentException e) {
            return renderError("参数格式错误");
        } catch (Exception e) {
            return renderError(e.getMessage());
        }
    }

    /**
     * 从列表中移除地图
     */
    @PostMapping("/removeMap")
    public Object removeMapFromList(@RequestParam("listId") String listIdStr,
                                    @RequestParam("mapId") String mapIdStr,
                                    @RequestParam("userId") String userIdStr) {
        try {
            UUID listId = UUID.fromString(listIdStr);
            UUID mapId = UUID.fromString(mapIdStr);
            UUID userId = UUID.fromString(userIdStr);

            boolean success = customListService.removeMapFromList(listId, mapId, userId);
            if (success) {
                return renderSuccess("移除成功");
            } else {
                return renderError("移除失败，请检查列表权限");
            }
        } catch (IllegalArgumentException e) {
            return renderError("参数格式错误");
        } catch (Exception e) {
            return renderError(e.getMessage());
        }
    }
}