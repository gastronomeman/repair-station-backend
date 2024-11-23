package com.repairstation.server.service;

import com.repairstation.domain.po.LinkItem;
import com.repairstation.domain.po.WebLink;

import java.util.List;

public interface WebLinkService {
    List<WebLink> findAll();

    void deleteWebLinkById(String id);

    void deleteLinkItemById(String id, String itemId);

    void createNewWebLink(WebLink webLink);

    void updateWebLinkById(WebLink webLink);

    void addLinkItem(String id, LinkItem newItem);

    void updateLinkItem(String id, LinkItem newItem);

    List<LinkItem> findByName(String name);
}
