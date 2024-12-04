package com.rs.controller.tool;

import com.rs.common.R;
import com.rs.domain.po.LinkItem;
import com.rs.domain.po.WebLink;
import com.rs.service.WebLinkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/link")
@Slf4j
public class WebLinkController {
    @Autowired
    WebLinkService webLinkService;

    @Cacheable(value = "webLinkCache", key = "0")
    @GetMapping
    public R<List<WebLink>> findAll() {
        return R.success(webLinkService.findAll());
    }

    @CacheEvict(value = "webLinkCache", key = "0")
    @DeleteMapping("/{id}")
    public R<String> delete(@PathVariable String id) {
        webLinkService.deleteWebLinkById(id);
        return R.success("删除成功");
    }

    @CacheEvict(value = "webLinkCache", key = "0")
    @DeleteMapping("/{id}/{itemId}")
    public R<String> delete(@PathVariable String id, @PathVariable String itemId) {
        webLinkService.deleteLinkItemById(id, itemId);
        return R.success("删除成功");
    }

    @CacheEvict(value = "webLinkCache", key = "0")
    @PostMapping
    public R<String> save(@RequestBody WebLink webLink) {
        webLinkService.createNewWebLink(webLink);
        return R.success("添加成功");
    }

    @CacheEvict(value = "webLinkCache", key = "0")
    @PostMapping("/{id}")
    public R<String> addLinkItem(@PathVariable String id, @RequestBody LinkItem linkItem) {
        webLinkService.addLinkItem(id, linkItem);
        return R.success("添加成功");
    }

    @CacheEvict(value = "webLinkCache", key = "0")
    @PutMapping
    public R<String> update(@RequestBody WebLink webLink) {
        webLinkService.updateWebLinkById(webLink);
        return R.success("修改成功");
    }

    @CacheEvict(value = "webLinkCache", key = "0")
    @PutMapping("/{id}")
    public R<String> updateItem(@PathVariable String id, @RequestBody LinkItem linkItem) {
        webLinkService.updateLinkItem(id, linkItem);
        return R.success("修改成功");
    }

    @Cacheable(value = "webLinkCache", key = "#name")
    @GetMapping("/by-name")
    public R<List<LinkItem>> findByName(@RequestParam String name) {
        List<LinkItem> list = webLinkService.findByName(name);
        return R.success(list);
    }
}
