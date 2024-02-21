package kfirmadoel.server_side.services;

import java.util.List;


import kfirmadoel.server_side.documents.ParentConInfo;
import kfirmadoel.server_side.repositories.ParentConRepository;

public class ParentConService {
    private final ParentConRepository parentConRepository;


    public ParentConService(ParentConRepository parentConRepository) {
        this.parentConRepository = parentConRepository;
    }

    public void createParentCon(String macAddr, int connectionCode) {
        ParentConInfo newParentCon = new ParentConInfo(macAddr, connectionCode);
        parentConRepository.save(newParentCon);
    }

    public ParentConInfo getParentConByMacAddr(String macAddr) {
        return parentConRepository.findByMacAddr(macAddr);
    }

    public List<ParentConInfo> getAllParentCons() {
        return parentConRepository.findAll();
    }

    public void updateParentConConnectionCode(String macAddr, int newConnectionCode) {
        ParentConInfo parentConInfo = parentConRepository.findByMacAddr(macAddr);
        if (parentConInfo != null) {
            parentConInfo.setConnectionCode(newConnectionCode);
            parentConRepository.save(parentConInfo);
        } else {
            // Handle case where parentCon with given macAddr doesn't exist
        }
    }

    public void deleteParentCon(String macAddr) {
        ParentConInfo parentConInfo = parentConRepository.findByMacAddr(macAddr);
        if (parentConInfo != null) {
            parentConRepository.delete(parentConInfo);
        } else {
            // Handle case where parentCon with given macAddr doesn't exist
        }
    }
    }