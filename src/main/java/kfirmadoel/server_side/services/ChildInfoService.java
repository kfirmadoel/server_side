package kfirmadoel.server_side.services;

import org.springframework.stereotype.Service;

import kfirmadoel.server_side.documents.ChildInfo;
import kfirmadoel.server_side.repositories.ChildInfoRepository;

@Service
public class ChildInfoService {

    private ChildInfoRepository childInfoRepository;

    public ChildInfoService(ChildInfoRepository childInfoRepository) {
        this.childInfoRepository = childInfoRepository;
    }

    public void createChild(String macAddr, String ipAddr) {
        ChildInfo childInfo = new ChildInfo(macAddr, ipAddr);
        childInfoRepository.save(childInfo);
    }

    public void createChild(ChildInfo childInfo) {
        childInfoRepository.save(childInfo);
    }

    public ChildInfo getChildByMacAddr(String macAddr) {
        return childInfoRepository.findByMacAddr(macAddr);
    }

    public void updateChildIpAddr(String macAddr, String newIpAddr) {
        ChildInfo childInfo = childInfoRepository.findByMacAddr(macAddr);
        if (childInfo != null) {
            childInfo.setIpAddr(newIpAddr);
            childInfoRepository.save(childInfo);
        } else {
            // Handle case where child with given macAddr doesn't exist
        }
    }

    public void deleteChild(String macAddr) {
        ChildInfo childInfo = childInfoRepository.findByMacAddr(macAddr);
        if (childInfo != null) {
            childInfoRepository.delete(childInfo);
        } else {
            // Handle case where child with given macAddr doesn't exist
        }
    }
    
}
