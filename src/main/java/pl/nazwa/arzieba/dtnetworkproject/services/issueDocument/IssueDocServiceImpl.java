package pl.nazwa.arzieba.dtnetworkproject.services.issueDocument;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.nazwa.arzieba.dtnetworkproject.dao.*;
import pl.nazwa.arzieba.dtnetworkproject.dto.DeviceDTO;
import pl.nazwa.arzieba.dtnetworkproject.dto.IssueDocumentDTO;
import pl.nazwa.arzieba.dtnetworkproject.dto.ShortPostDTO;
import pl.nazwa.arzieba.dtnetworkproject.model.Author;
import pl.nazwa.arzieba.dtnetworkproject.model.IssueDocument;
import pl.nazwa.arzieba.dtnetworkproject.model.IssueFiles;
import pl.nazwa.arzieba.dtnetworkproject.model.PostLevel;
import pl.nazwa.arzieba.dtnetworkproject.services.device.DeviceService;
import pl.nazwa.arzieba.dtnetworkproject.services.issueFiles.UploadPathService;
import pl.nazwa.arzieba.dtnetworkproject.services.shortPost.ShortPostService;
import pl.nazwa.arzieba.dtnetworkproject.utils.calendar.CalendarUtil;
import pl.nazwa.arzieba.dtnetworkproject.utils.exceptions.IssueDocumentNotFoundException;
import pl.nazwa.arzieba.dtnetworkproject.utils.issueDocument.IssueDocMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class IssueDocServiceImpl implements IssueDocService {

    private DeviceDAO deviceDAO;
    private DamageDAO damageDAO;
    private IssueDocumentDAO issueDocumentDAO;
    private DeviceCardDAO deviceCardDAO;
    private UploadPathService uploadPathService;
    private IssueFilesDAO issueFilesDAO;
    private DeviceService deviceService;
    private ShortPostService postService;
    @Value("${my.pagesize}")
    int pagesize;

    @Autowired
    public IssueDocServiceImpl(DeviceDAO deviceDAO, DamageDAO damageDAO, IssueDocumentDAO issueDocumentDAO, DeviceCardDAO deviceCardDAO, UploadPathService uploadPathService, IssueFilesDAO issueFilesDAO, DeviceService deviceService, ShortPostService postService) {
        this.deviceDAO = deviceDAO;
        this.damageDAO = damageDAO;
        this.issueDocumentDAO = issueDocumentDAO;
        this.deviceCardDAO = deviceCardDAO;
        this.uploadPathService = uploadPathService;
        this.issueFilesDAO = issueFilesDAO;
        this.deviceService = deviceService;
        this.postService = postService;
    }

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

        IssueDocument saved = IssueDocMapper.map(documentDTO, damageDAO);
        IssueFiles files = new IssueFiles();

        if(issueDocumentDAO.existsByIssueSignature(documentDTO.getIssueSignature()) && issueDocumentDAO.findByIssueSignature(documentDTO.getIssueSignature()).getDamage()!=null ) {
            saved.setDamage(issueDocumentDAO.findByIssueSignature(documentDTO.getIssueSignature()).getDamage());
            saved.setIssueId(issueDocumentDAO.findByIssueSignature(documentDTO.getIssueSignature()).getIssueId());
        }
        if(issueDocumentDAO.existsByIssueSignature(documentDTO.getIssueSignature()) ) {
            saved.setIssueId(issueDocumentDAO.findByIssueSignature(documentDTO.getIssueSignature()).getIssueId());
        }
            IssueDocument saved2 = issueDocumentDAO.save(saved);

        if(saved!=null && documentDTO.getIssueFiles()!=null && documentDTO.getIssueFiles().size()>0){

            for (MultipartFile file: documentDTO.getIssueFiles()) {

                String fileName = file.getOriginalFilename().replaceAll(" ","_");
                String modifiedFileName = FilenameUtils.getBaseName(fileName).replaceAll(" ","_")+"_"+System.currentTimeMillis()+"."+FilenameUtils.getExtension(fileName);
                File storeFile = uploadPathService.getFilePath(modifiedFileName, "files");

                if(storeFile!=null){
                    try {
                        FileUtils.writeByteArrayToFile(storeFile,file.getBytes());
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }

                files.setFileExtension(FilenameUtils.getExtension(fileName));
                files.setFileName(fileName);
                files.setFileNameNoExt(modifiedFileName.substring(0,modifiedFileName.lastIndexOf('.')));
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

            sortedSet.addAll(years);           //test

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

    @Override
    public String showDocsforDevice(String inventNumber, Model model, int page) {
        List<IssueDocumentDTO> docs = deviceService.getIssueDocuments(inventNumber);
        DeviceDTO dto = deviceService.generateMainViewForDevice(inventNumber);
        List<IssueDocumentDTO> page1= findByDevice(inventNumber,page-1,pagesize);
        List<String> pageNumbers = docs.stream().map(d->d.getInventNumber()).collect(Collectors.toList());
        int numberOfPages = (numberByDevice(inventNumber))/pagesize + 1;
        int i = 2;
        int lastPage = 1;
        //prevents empty last page
        //zapobiega tworzeniu ostatniej pustej strony
        if(numberByDevice(inventNumber)%pagesize==0){
            numberOfPages--;
        }
        List<Integer> morePages = new LinkedList<>();

        while(i<=numberOfPages){
            morePages.add(i);
            i++;
            lastPage++;
        }

        model.addAttribute("numbers",pageNumbers);
        model.addAttribute("classActiveSettings","active");
        model.addAttribute("pages",morePages);
        model.addAttribute("currentPage",page);
        model.addAttribute("lastPage",lastPage);
        model.addAttribute("docs",page1);
        model.addAttribute("amount", docs.size());
        model.addAttribute("dto", dto);
        return "devices/getAllDocs";
    }


    @Override
    public List<IssueFiles> getFilesForDoc(String signature) {
        return issueFilesDAO.findAllByIssueDocument_IssueSignature(signature);
    }

    @Override
    public String addFormDevice(String inventNumber, Model model) {
        IssueDocumentDTO issueDocumentDTO = new IssueDocumentDTO();
        String text = deviceDAO.findByInventNumber(inventNumber).getDeviceDescription()
                +" "+deviceDAO.findByInventNumber(inventNumber).getRoom();
        issueDocumentDTO.setIssueDate(CalendarUtil.cal2string(Calendar.getInstance()));
        model.addAttribute("newDoc", issueDocumentDTO);
        model.addAttribute("inventNumber", inventNumber);
        model.addAttribute("text",text);
        return "documents/addDocFormDev";
    }

    @Override
    public String addFormDamage(Integer damageId, Model model) {
        IssueDocumentDTO dto = new IssueDocumentDTO();
        String inventNumber = damageDAO.findById(damageId).orElse(null).getDevice().getInventNumber();
        String text = deviceDAO.findByInventNumber(inventNumber).getDeviceDescription()
                +" "+deviceDAO.findByInventNumber(inventNumber).getRoom();

        dto.setIssueDate(CalendarUtil.cal2string(Calendar.getInstance()));
        model.addAttribute("newDoc", dto);
        model.addAttribute("inventNumber", inventNumber);
        model.addAttribute("text",text);
        model.addAttribute("damageId",damageId);
        return "documents/addDocFormDam";
    }

    @Override
    @Transactional
    public String createDocumentForDamage(IssueDocumentDTO issueDocumentDTO, BindingResult bindingResult, Model model, HttpServletRequest request) {
        if(bindingResult.hasFieldErrors()) {
            List<FieldError> allErrors;
            allErrors = bindingResult.getFieldErrors();

            if(issueDocumentDAO.existsByIssueSignature(issueDocumentDTO.getIssueSignature())){
                FieldError fieldError = new FieldError("newDoc","issueSignature", issueDocumentDTO.getIssueSignature(),false,null,null ,"Zamówienie o tej sygnaturze już istnieje w bazie danych!");
                bindingResult.addError(fieldError);
            }
            model.addAttribute("bindingResult", bindingResult);
            model.addAttribute("errors", allErrors);
            model.addAttribute("errorsAmount", allErrors.size());
            return addFormDamageErr(issueDocumentDTO.getDamageId(), model,issueDocumentDTO);
        }

        if(issueDocumentDAO.existsByIssueSignature(issueDocumentDTO.getIssueSignature())){
            FieldError fieldError = new FieldError("newDoc","issueSignature", issueDocumentDTO.getIssueSignature(),false,null,null ,"Zamówienie o tej sygnaturze już istnieje w bazie danych!");
            bindingResult.addError(fieldError);
            model.addAttribute("bindingResult", bindingResult);
            model.addAttribute("errors", fieldError);
            model.addAttribute("errorsAmount", 1);
            return addFormDamageErr(issueDocumentDTO.getDamageId(), model,issueDocumentDTO);
        }

        create(issueDocumentDTO);
        ShortPostDTO shortPostDTO = new ShortPostDTO(Author.DTN,"Wprowadzono nowe zamówienie dla usterki! [SYSTEM]",CalendarUtil.cal2string(Calendar.getInstance()),
                issueDocumentDTO.getInventNumber(),false,PostLevel.INFO );
        postService.create(shortPostDTO);
        return "redirect:/dtnetwork";
    }

    @Override
    @Transactional
    public String createDocumentForDevice(IssueDocumentDTO issueDocumentDTO, BindingResult bindingResult, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        if(bindingResult.hasFieldErrors()) {
            List<FieldError> allErrors;
            allErrors = bindingResult.getFieldErrors();

            if(issueDocumentDAO.existsByIssueSignature(issueDocumentDTO.getIssueSignature())){
                FieldError fieldError = new FieldError("newDoc","issueSignature", issueDocumentDTO.getIssueSignature(),false,null,null ,"Zamówienie o tej sygnaturze już istnieje w bazie danych!");
                bindingResult.addError(fieldError);
            }
            model.addAttribute("bindingResult", bindingResult);
            model.addAttribute("errors", allErrors);
            model.addAttribute("errorsAmount", allErrors.size());
            return addFormDeviceErr(issueDocumentDTO.getInventNumber(), model,issueDocumentDTO);
        }

        if(issueDocumentDAO.existsByIssueSignature(issueDocumentDTO.getIssueSignature())){
            FieldError fieldError = new FieldError("newDoc","issueSignature", issueDocumentDTO.getIssueSignature(),false,null,null ,"Zamówienie o tej sygnaturze już istnieje w bazie danych!");

            bindingResult.addError(fieldError);
            model.addAttribute("bindingResult", bindingResult);
            model.addAttribute("errors", fieldError);
            model.addAttribute("errorsAmount", 1);
            return addFormDeviceErr(issueDocumentDTO.getInventNumber(), model,issueDocumentDTO);
        }
        ShortPostDTO postDTO = new ShortPostDTO(Author.DTN,"Wprowadzono nowe zamówienie dla urządzenia! [SYSTEM]",
                CalendarUtil.cal2string(Calendar.getInstance()), issueDocumentDTO.getInventNumber(),false,PostLevel.INFO);
        create(issueDocumentDTO);
        postService.create(postDTO);
        return "redirect:/dtnetwork";
    }

    public String addFormDamageErr(@PathVariable Integer damageId, Model model, IssueDocumentDTO documentDTO){
        String inventNumber = damageDAO.findById(damageId).orElse(null).getDevice().getInventNumber();
        String text = deviceDAO.findByInventNumber(inventNumber).getDeviceDescription()
                +" "+deviceDAO.findByInventNumber(inventNumber).getRoom();

        model.addAttribute("newDoc", documentDTO);
        model.addAttribute("inventNumber", inventNumber);
        model.addAttribute("text",text);
        model.addAttribute("damageId",damageId);
        return "documents/addDocFormDam";
    }

    public String addFormDeviceErr(@PathVariable String inventNumber, Model model, IssueDocumentDTO issueDocumentDTO){

        String text = deviceDAO.findByInventNumber(inventNumber).getDeviceDescription()
                +" "+deviceDAO.findByInventNumber(inventNumber).getRoom();

        model.addAttribute("newDoc",issueDocumentDTO);
        model.addAttribute("inventNumber", inventNumber);
        model.addAttribute("text",text);

        return "documents/addDocFormDev";
    }

}
