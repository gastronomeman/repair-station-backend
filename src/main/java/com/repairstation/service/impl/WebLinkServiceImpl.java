package com.repairstation.service.impl;

import com.repairstation.domain.po.LinkItem;
import com.repairstation.domain.po.WebLink;
import com.repairstation.service.WebLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class WebLinkServiceImpl implements WebLinkService {
    @Autowired
    MongoTemplate mongoTemplate;

    public List<WebLink> findAll() {
        List<WebLink> webLinks = mongoTemplate.findAll(WebLink.class);

        for (WebLink webLink : webLinks) {
            webLink.getList().sort(Comparator.comparing(LinkItem::getName));
        }

        return webLinks;
    }

    public void deleteWebLinkById(String id) {
        // 创建查询条件，根据 id 查找文档
        Query query = new Query(Criteria.where("id").is(id));

        // 删除匹配的文档
        mongoTemplate.remove(query, WebLink.class);
    }

    public List<LinkItem> findByName(String name) {
        // 使用正则表达式进行模糊查询，忽略大小写
        Query query = new Query(Criteria.where("list.name").regex(".*" + name + ".*", "i"));

        // 查找文档，获取符合条件的所有 'list' 中的项
        List<WebLink> results = mongoTemplate.find(query, WebLink.class, "web_link");

        // 从结果中提取出所有符合条件的 LinkItem
        return results.stream()
                .flatMap(webLink -> webLink.getList().stream()
                        .filter(linkItem -> linkItem.getName().toLowerCase().contains(name.toLowerCase()))
                )
                .collect(Collectors.toList());
    }

    public void deleteLinkItemById(String id, String itemId) {
        // 创建查询条件，根据 title 查找文档
        Query query = new Query(Criteria.where("id").is(id));

        // 创建更新操作，使用 $pull 删除匹配的 list 中的项
        Update update = new Update().pull("list", new Query(Criteria.where("id").is(itemId)));

        // 更新文档
        mongoTemplate.updateFirst(query, update, WebLink.class);
    }

    public void createNewWebLink(WebLink webLink) {

        mongoTemplate.save(webLink, "web_link"); // 插入到 'web_link' 集合中
    }

    public void addLinkItem(String id, LinkItem newItem) {
        newItem.setId(UUID.randomUUID().toString());
        // 根据 title 查找对应的文档
        Query query = new Query(Criteria.where("id").is(id));

        // 使用更新操作将新的 LinkItem 添加到 list 数组中
        Update update = new Update().addToSet("list", newItem);

        // 执行更新操作
        mongoTemplate.updateFirst(query, update, WebLink.class);
    }

    public void updateWebLinkById(WebLink webLink) {
        // 创建查询条件，根据 ID 查找文档
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(webLink.getId()));

        // 创建更新操作
        Update update = new Update();
        update.set("title", webLink.getTitle()); // 更新标题

        // 执行更新
        mongoTemplate.updateFirst(query, update, WebLink.class, "web_link");
    }

    public void updateLinkItem(String id, LinkItem newItem) {
        Query query = new Query(Criteria.where("_id").is(id).and("list._id").is(newItem.getId()));
        Update update = new Update()
                .set("list.$.name", newItem.getName())
                .set("list.$.url", newItem.getUrl())
                .set("list.$.photo", newItem.getPhoto());

        mongoTemplate.updateFirst(query, update, "web_link");
    }
}
