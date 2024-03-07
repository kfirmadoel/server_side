package kfirmadoel.server_side.services;

import java.util.List;

import org.springframework.stereotype.Service;

import kfirmadoel.server_side.documents.ChildForPar;
import kfirmadoel.server_side.documents.ParentConInfo;
import kfirmadoel.server_side.repositories.ParentConRepository;

@Service
public class ParentConService {
    private final ParentConRepository parentConRepository;

    public ParentConService(ParentConRepository parentConRepository) {
        this.parentConRepository = parentConRepository;
    }

    public void createParentCon(String macAddr, int connectionCode) {
        ParentConInfo newParentCon = new ParentConInfo(macAddr);
        parentConRepository.save(newParentCon);
    }

    public ParentConInfo getParentConByEmail(String email) {
        return parentConRepository.findByEmail(email);
    }

    public List<ParentConInfo> getAllParentCons() {
        return parentConRepository.findAll();
    }

    public void updateParentConConnectionCode(String email) {
        ParentConInfo parentConInfo = parentConRepository.findByEmail(email);
        if (parentConInfo != null) {
            parentConRepository.save(parentConInfo);
        } else {
            // Handle case where parentCon with given macAddr doesn't exist
        }
    }

    public void deleteParentCon(String email) {
        ParentConInfo parentConInfo = parentConRepository.findByEmail(email);
        if (parentConInfo != null) {
            parentConRepository.delete(parentConInfo);
        } else {
            // Handle case where parentCon with given macAddr doesn't exist
        }
    }

    public ChildForPar getChildInfoByEmailAndName(String email,String name)
    {
        ParentConInfo parentconinConInfo=getParentConByEmail(email);
        for (int i = 0; i < parentconinConInfo.getChildsMac().size(); i++) {
            if(name.equals(parentconinConInfo.getChildsMac().get(i).getName()))
            return parentconinConInfo.getChildsMac().get(i);
        }
        return null;
    }

    public void updateParentCon(ParentConInfo parentConInfo) {
        parentConRepository.save(parentConInfo);
    }
}