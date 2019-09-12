package pl.nazwa.arzieba.dtnetworkproject.services.issueDocument;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;
import pl.nazwa.arzieba.dtnetworkproject.dao.*;
import pl.nazwa.arzieba.dtnetworkproject.dto.IssueDocumentDTO;
import pl.nazwa.arzieba.dtnetworkproject.model.IssueDocument;
import pl.nazwa.arzieba.dtnetworkproject.model.IssueFiles;
import pl.nazwa.arzieba.dtnetworkproject.services.issueFiles.UploadPathService;
import pl.nazwa.arzieba.dtnetworkproject.utils.exceptions.IssueDocumentNotFoundException;
import pl.nazwa.arzieba.dtnetworkproject.utils.issueDocument.IssueDocMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class IssueDocServiceImpl implements IssueDocService {
    @Autowired
    public IssueDocServiceImpl(DeviceDAO deviceDAO, DamageDAO damageDAO, IssueDocumentDAO issueDocumentDAO, DeviceCardDAO deviceCardDAO, UploadPathService uploadPathService, IssueFilesDAO issueFilesDAO) {
        this.deviceDAO = deviceDAO;
        this.damageDAO = damageDAO;
        this.issueDocumentDAO = issueDocumentDAO;
        this.deviceCardDAO = deviceCardDAO;
        this.uploadPathService = uploadPathService;
        this.issueFilesDAO = issueFilesDAO;
    }

    private DeviceDAO deviceDAO;
    private DamageDAO damageDAO;
    private IssueDocumentDAO issueDocumentDAO;
    private DeviceCardDAO deviceCardDAO;
    private UploadPathService uploadPathService;
    private IssueFilesDAO issueFilesDAO;



    @Override
    public List<IssueDocumentDTO> findAll() {
        return issueDocumentDAO.findAll().stream()
                .map(IssueDocMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public IssueDocumentDTO findBySignature(String signature) {
        if(issueDocumentDAO.existsByIssueSignature(signature))
        return IssueDocMapper.map(issueDocumentDAO.findByIssueSignature(signature));
        else throw new IssueDocumentNotFoundException("Issue document not found");
    }

    @Override
    public List<IssueDocumentDTO> findByInventNumber(String inventNumber) {
        return issueDocumentDAO.findByInventNumber(inventNumber)
                .stream()
                .map(IssueDocMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public List<IssueDocumentDTO> findByYear(int year,int page, int size) {

        Calendar startDate = new GregorianCalendar(year,0,1);
        Calendar endDate = new GregorianCalendar(year,11,31);

        List<IssueDocumentDTO> documentPage = issueDocumentDAO.findAllByIssueDateBetween(
                PageRequest.of(page,size, Sort.Direction.DESC,"issueDate"),
                startDate,endDate)
                .get()
                .map(IssueDocMapper::map)
                .collect(Collectors.toList());

        /*List<IssueDocumentDTO> list= issueDocumentDAO.findAll().stream()
                .filter(d->d.getIssueDate().get(Calendar.YEAR) ==year)
                .map(d->IssueDocMapper.map(d))
                .collect(Collectors.toList());*/

        return documentPage;
    }

    @Override
    public List<IssueDocumentDTO> findByDamageId(Integer id) {
        return issueDocumentDAO.findByDamage_DamageId(id).stream()
                .map(IssueDocMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public IssueDocumentDTO create(IssueDocumentDTO documentDTO) {
        IssueDocument saved = IssueDocMapper.map(documentDTO,damageDAO);
        IssueDocument saved2 = issueDocumentDAO.save(saved);


        System.out.println();

        if(saved!=null && documentDTO.getIssueFiles()!=null && documentDTO.getIssueFiles().size()>0){

            for (MultipartFile file: documentDTO.getIssueFiles() ) {

                String fileName = file.getOriginalFilename();
                String modifiedFileName = FilenameUtils.getBaseName(fileName)+"_"+System.currentTimeMillis()+"."+FilenameUtils.getExtension(fileName);
                File storeFile = uploadPathService.getFilePath(modifiedFileName, "files");

                if(storeFile!=null){
                    try {
                        FileUtils.writeByteArrayToFile(storeFile,file.getBytes());
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }



                IssueFiles files = new IssueFiles();
                files.setFileExtension(FilenameUtils.getExtension(fileName));
                files.setFileName(fileName);
                files.setModifiedFileName(modifiedFileName);
                files.setIssueDocument(saved2);
                issueFilesDAO.save(files);


            }

        }

        return IssueDocMapper.map(saved);
    }

    @Override
    public IssueDocumentDTO createWithFiles(IssueDocumentDTO documentDTO){

        return null;
    }

    @Override
    public IssueDocumentDTO update(IssueDocumentDTO documentDTO) {
        if(!issueDocumentDAO.existsByIssueSignature(documentDTO.getIssueSignature())){
            return  create(documentDTO);

        } else{
            IssueDocument toBeUpdated = issueDocumentDAO.findByIssueSignature(documentDTO.getIssueSignature());
            toBeUpdated.setIssueDetails(documentDTO.getIssueDetails());
            toBeUpdated.setValue(documentDTO.getValue());
            return IssueDocMapper.map(toBeUpdated);
        }
    }

    @Override
    public IssueDocumentDTO remove(String signature) {
        if (!issueDocumentDAO.existsByIssueSignature(signature)) {
            throw new IssueDocumentNotFoundException("Issue Document not found!");
        } else {
            IssueDocumentDTO removed = findBySignature(signature);
            issueDocumentDAO.delete(issueDocumentDAO.findByIssueSignature(signature));
            removed.setIssueTittle("Removed " + removed.getIssueTittle());
            return removed;

        }
    }

    @Override
    public List<IssueDocumentDTO> findByDevice(String inv, int page, int size) {
        return issueDocumentDAO.findAllByInventNumber(inv,PageRequest.of(page, size, Sort.Direction.DESC,"issueDate"))
                .get().map(IssueDocMapper::map)
                .collect(Collectors.toList());
    }

    @Override
        public Set<Integer> setOfYears(){

            List<Integer> yearsList= issueDocumentDAO.findAll().stream()
                    .map(d->d.getIssueDate().get( Calendar.YEAR))
                    .collect(Collectors.toList());
            Set<Integer> years =yearsList.stream().collect(Collectors.toSet());
            TreeSet<Integer> sortedSet = new TreeSet<>();
            sortedSet.addAll(years);
            //test

            return sortedSet.descendingSet();
        }


        @Override

        public Set<Integer> subSet(){

            if(setOfYears().size()<=5) return setOfYears();
            List<Integer> yearsList= issueDocumentDAO.findAll().stream()
                    .map(d->d.getIssueDate().get( Calendar.YEAR))
                    .collect(Collectors.toList());
            TreeSet<Integer> sortedSet = new TreeSet<>();
            sortedSet.addAll(yearsList);

            List<Integer> sortedList = new LinkedList<>();
            sortedList.addAll(sortedSet.descendingSet());

            TreeSet<Integer> sortedSubSet = new TreeSet<>();


            if(sortedList.size()>0) {
                for (int i = 0; i < 5; i++) {
                    sortedSubSet.add(sortedList.get(i));
                }

                return sortedSubSet.descendingSet();
            }
            return null;

        }

    @Override
    public int numberByDevice(String inv) {
        return (int) issueDocumentDAO.findByInventNumber(inv).stream().count();
    }

    @Override
        public int numberByYear(int year){
        long l = issueDocumentDAO.findAll().stream()
                .filter(d->d.getIssueDate().get(Calendar.YEAR)==year)
                .count();

        return (int) l;
        }



}
