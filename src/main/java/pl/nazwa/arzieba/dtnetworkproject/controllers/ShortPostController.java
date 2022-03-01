package pl.nazwa.arzieba.dtnetworkproject.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import pl.nazwa.arzieba.dtnetworkproject.configuration.MyPropertiesConfig;
import pl.nazwa.arzieba.dtnetworkproject.dao.DeviceDAO;
import pl.nazwa.arzieba.dtnetworkproject.dao.ShortPostDAO;
import pl.nazwa.arzieba.dtnetworkproject.dto.DeviceDTO;
import pl.nazwa.arzieba.dtnetworkproject.dto.ShortPostDTO;
import pl.nazwa.arzieba.dtnetworkproject.model.Author;
import pl.nazwa.arzieba.dtnetworkproject.model.Device;
import pl.nazwa.arzieba.dtnetworkproject.model.ShortPost;
import pl.nazwa.arzieba.dtnetworkproject.services.device.DeviceService;
import pl.nazwa.arzieba.dtnetworkproject.services.shortPost.ShortPostService;
import pl.nazwa.arzieba.dtnetworkproject.utils.calendar.CalendarUtil;
import pl.nazwa.arzieba.dtnetworkproject.utils.device.DeviceMapper;
import pl.nazwa.arzieba.dtnetworkproject.utils.enums.ListOfEnumValues;
import pl.nazwa.arzieba.dtnetworkproject.utils.mail.EmailConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/posts")
public class ShortPostController implements WebMvcConfigurer {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private ShortPostDAO postDAO;
    private DeviceDAO deviceDAO;
    private ShortPostService postService;
    private DeviceService deviceService;
    private MyPropertiesConfig myPropertiesConfig;
    @Value("${my.pagesize}")
    int pagesize;
    private EmailConfiguration emailConfiguration;
    private ApplicationArguments applicationArguments;
    @Value("${my.mailReceivers}")
    private String[] mailReceivers;

    @Autowired
    public ShortPostController(ShortPostDAO postDAO, DeviceDAO deviceDAO, ShortPostService postService, DeviceService deviceService, MyPropertiesConfig myPropertiesConfig, EmailConfiguration emailConfiguration, ApplicationArguments applicationArguments) {
        this.postDAO = postDAO;
        this.deviceDAO = deviceDAO;
        this.postService = postService;
        this.deviceService = deviceService;
        this.myPropertiesConfig = myPropertiesConfig;
        this.emailConfiguration = emailConfiguration;
        this.applicationArguments = applicationArguments;
    }

    @GetMapping("/devices/{inv}/{page}")
    public String getAllForDevice(@PathVariable String inv, Model model, @PathVariable int page) {

        DeviceDTO dto = DeviceMapper.map(deviceDAO.findByInventNumber(inv));
        List<ShortPost> all = postDAO.findAllByDevice_InventNumber(inv);
        Map<Integer, ShortPostDTO> mapa = postService.findAllByDevice(inv, page - 1, pagesize);
        int amount = postService.numberByDevice(inv);
        int numberOfPages = (postService.numberByDevice(inv)) / pagesize + 1;
        List<Integer> morePages = new LinkedList<>();
        int i = 2;
        int lastPage = 1;

        if (postService.numberByDevice(inv) % pagesize == 0) {
            numberOfPages--;
        }

        while (i <= numberOfPages) {
            morePages.add(i);
            i++;
            lastPage++;
        }

        model.addAttribute("amount", amount);
        model.addAttribute("posts", mapa);
        model.addAttribute("dto", dto);
        model.addAttribute("classActiveSettings", "active");
        model.addAttribute("lastPage", lastPage);
        model.addAttribute("pages", morePages);
        model.addAttribute("currentPage", page);
        model.addAttribute("inv", inv);

        return "posts/allPostsForDevice";
    }

    @PostMapping("/add")
    public String create(Model model, ShortPostDTO dto) {

        postService.create(dto);

        return "error";
    }

    @PostMapping("/addAsModel")
    public String create2(Model model, @Valid @ModelAttribute("newPost") ShortPostDTO dto, BindingResult result, HttpServletRequest request) {

        if (result.hasFieldErrors()) {
            List<FieldError> allErrors;
            allErrors = result.getFieldErrors();

            model.addAttribute("bindingResult", result);
            model.addAttribute("errors", allErrors);
            model.addAttribute("errorsAmount", allErrors.size());

            return addFormErr(model, dto);
        }

        postService.create(dto);

        return "redirect:/dtnetwork";
    }

    @PostMapping("/addAsModel/stay")
    public String create3( @Valid @ModelAttribute ShortPostDTO shortPostDTO, BindingResult bindingResult,Model model, HttpServletRequest request) {

        if (bindingResult.hasFieldErrors()) {
            List<FieldError> allErrors;
            allErrors = bindingResult.getFieldErrors();

            model.addAttribute("bindingResult", bindingResult);
            model.addAttribute("errors", allErrors);
            model.addAttribute("errorsAmount", allErrors.size());

            return addFormErr(model, shortPostDTO.getInventNumber(), shortPostDTO);
        }

        postService.create(shortPostDTO);

        return "redirect:/posts/devices/" + shortPostDTO.getInventNumber()+"/1";
    }

    @GetMapping("/delete/{id}")
    public String remove(@PathVariable Integer id, Model model) {

        postService.remove(id);

        return "redirect:/dtnetwork";
    }

    @GetMapping("/delete/{id}/stay")
    public String removeAndStay(@PathVariable Integer id, Model model, HttpServletRequest request) {

        postService.remove(id);

        return "redirect:" + request.getHeader("Referer");
    }

    @GetMapping("/addForm")
    public String addForm(Model model) {

        ShortPostDTO dto = new ShortPostDTO();
        Map<String, String> mapa = new HashMap<>();
        List<String> keys = deviceDAO.findAll().stream().map(d -> d.getInventNumber()).collect(Collectors.toList());

        for (String key : keys) {
            Device device = deviceDAO.findByInventNumber(key);
            mapa.put(key, device.getDeviceDescription());
        }

        dto.setDate(CalendarUtil.cal2string(Calendar.getInstance() ));
        model.addAttribute("devices", mapa);
        model.addAttribute("newPost", dto);
        model.addAttribute("authors", ListOfEnumValues.authors);

        return "posts/addPostForm";
    }

    @GetMapping("/addForm/{inventNumber}")
    public String addForm(Model model, @PathVariable String inventNumber) {

        ShortPostDTO dto = new ShortPostDTO();
        String text = deviceDAO.findByInventNumber(inventNumber).getDeviceDescription()
                + " " + deviceDAO.findByInventNumber(inventNumber).getRoom();

        dto.setDate(CalendarUtil.cal2string(Calendar.getInstance() ));
        model.addAttribute("text", text);
        model.addAttribute("shortPostDTO", dto);
        model.addAttribute("authors", ListOfEnumValues.authors);
        model.addAttribute("inventNumber", inventNumber);

        return "posts/addPostFormInv";
    }

    @GetMapping("/all/{year}/{page}")
    public String allByYear(Model model, @PathVariable int year, @PathVariable int page) {

        Map<Integer, ShortPostDTO> mapa = postService.findAll(year, page - 1, 10);
        int numberOfPages = (postService.numberByYear(year)) / 10 + 1;
        List<Integer> morePages = new LinkedList<>();
        List<ShortPost> allPosts = postService.findAllByYear(year);
        int amount = postService.numberByYear(year);
        int i = 2;
        int lastPage = 1;

        if (postService.numberByYear(year) % 10 == 0) {
            numberOfPages--;
        }

        for (ShortPost post : allPosts) {
            if (post.getContent().contains("kliknięciu")) {
                amount--;
            }
        }

        while (i <= numberOfPages) {
            morePages.add(i);
            i++;
            lastPage++;
        }

        model.addAttribute("amount", amount);
        model.addAttribute("classActiveSettings", "active");
        model.addAttribute("posts", mapa);
        model.addAttribute("pages", morePages);
        model.addAttribute("currentPage", page);
        model.addAttribute("year", year);
        model.addAttribute("lastPage", lastPage);

        return "posts/allByYear";
    }

    public String addFormErr(Model model, ShortPostDTO dto) {

        Map<String, String> mapa = new HashMap<>();
        List<String> keys = deviceDAO.findAll().stream().map(d -> d.getInventNumber()).collect(Collectors.toList());

        for (String key : keys) {
            Device device = deviceDAO.findByInventNumber(key);
            mapa.put(key, device.getDeviceDescription());
        }

        model.addAttribute("devices", mapa);
        model.addAttribute("newPost", dto);
        model.addAttribute("authors", ListOfEnumValues.authors);

        return "posts/addPostForm";
    }

    public String addFormErr(Model model, @PathVariable String inventNumber, ShortPostDTO dto) {

        String text = deviceDAO.findByInventNumber(inventNumber).getDeviceDescription()
                + " " + deviceDAO.findByInventNumber(inventNumber).getRoom();

        model.addAttribute("shortPostDTO", dto);
        model.addAttribute("authors", ListOfEnumValues.authors);
        model.addAttribute("inventNumber", inventNumber);
        model.addAttribute("text", text);

        return "posts/addPostFormInv";
    }

    @GetMapping("/search")
    public String search(Model model, @ModelAttribute("search") String search ){

        Map<Integer,ShortPostDTO> mapa= postService.searchContent(search);
        int amount = mapa.keySet().size();

        model.addAttribute("mapa", mapa);
        model.addAttribute("keys", mapa.keySet());
        model.addAttribute("amount", amount);
        model.addAttribute("phrase",search);

        return "posts/searchResults";
    }

    @GetMapping("/edit/{id}")
    public String editPost(Model model,@PathVariable Integer id){

        ShortPostDTO dto = postService.findById(id);
        Map<String, String> mapa = new HashMap<>();
        List<String> keys = deviceDAO.findAll().stream().map(d -> d.getInventNumber()).collect(Collectors.toList());

        for (String key : keys) {
            Device device = deviceDAO.findByInventNumber(key);
            mapa.put(key, device.getDeviceDescription());
        }

        model.addAttribute("newPost", dto);
        model.addAttribute("authors", ListOfEnumValues.authors);
        model.addAttribute("devices", mapa);
        model.addAttribute("id", id);

        return "posts/editPostForm";
    }

    @GetMapping("/removeSystem")
    public String removeSystem(){

        List<ShortPost> posts = postDAO.findAll();
        ShortPostDTO dto = new ShortPostDTO();

        for (ShortPost post: posts) {
            if (post.getContent().contains("SYSTEM")) postDAO.delete(post);
        }

        dto.setDate(CalendarUtil.cal2string(Calendar.getInstance()));
        dto.setInventNumber("DTN");
        dto.setAuthor(Author.DTN);
        dto.setContent("Usunięto posty systemowe! [SYSTEM]");
        dto.setForDamage(false);
        postService.create(dto);

        return "redirect:/dtnetwork";
    }

    @PostMapping("/edit/{id}")
    public String saveEditedPost( @Valid @ModelAttribute("newPost") ShortPostDTO shortPostDTO,BindingResult bindingResult, Model model, @PathVariable Integer id ){

        ShortPost post = postDAO.findByPostId(id);
        Calendar calendar= CalendarUtil.string2cal(shortPostDTO.getDate());

        if (bindingResult.hasFieldErrors()) {
            List<FieldError> allErrors;
            allErrors = bindingResult.getFieldErrors();

            model.addAttribute("bindingResult", bindingResult);
            model.addAttribute("errors", allErrors);
            model.addAttribute("errorsAmount", allErrors.size());

            return editFormErr(model, shortPostDTO.getInventNumber(), shortPostDTO, id);
        }

        post.setAuthor(shortPostDTO.getAuthor());
        post.setContent(shortPostDTO.getContent());
        post.setPostDate(CalendarUtil.string2cal(shortPostDTO.getDate()));
        post.setDevice(deviceDAO.findByInventNumber(shortPostDTO.getInventNumber()));
        post.setDate(calendar.getTime());
        postDAO.save(post);
        try {
            if (!shortPostDTO.getContent().contains("[SYSTEM]")&& applicationArguments.getSourceArgs()[0].contains("mail") ){
                emailConfiguration.sendMail(mailReceivers,"Wpis dla: "+deviceDAO.findByInventNumber(shortPostDTO.getInventNumber()).getDeviceDescription()+ " w: "+deviceDAO.findByInventNumber(shortPostDTO.getInventNumber()).getRoom().name()+" [UPDATE]",shortPostDTO.getContent()
                        ,shortPostDTO.getAuthor().name());
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            logger.warn("MailSender mode is not set!");
        }

        return "redirect:/dtnetwork";
    }

    @GetMapping("/modalPosts/")
    public String modalPosts (Model model, @ModelAttribute("posts") int year ){

        return allByYear(model,year,1);
    }

    public String editFormErr(Model model, String inventNumber, ShortPostDTO dto, Integer id) {

        Map<String, String> mapa = new HashMap<>();
        List<String> keys = deviceDAO.findAll().stream().map(d -> d.getInventNumber()).collect(Collectors.toList());

        for (String key : keys) {
            Device device = deviceDAO.findByInventNumber(key);
            mapa.put(key, device.getDeviceDescription());
        }

        model.addAttribute("newPost", dto);
        model.addAttribute("authors", ListOfEnumValues.authors);
        model.addAttribute("devices", mapa);
        model.addAttribute("id", id);

        return "posts/editPostForm";
    }
}
