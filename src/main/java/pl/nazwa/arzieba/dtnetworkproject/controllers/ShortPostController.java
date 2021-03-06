package pl.nazwa.arzieba.dtnetworkproject.controllers;

import org.springframework.beans.factory.annotation.Value;
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
import pl.nazwa.arzieba.dtnetworkproject.utils.shortPost.ShortPostMapper;
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

    private ShortPostDAO postDAO;
    private DeviceDAO deviceDAO;
    private ShortPostService postService;
    private DeviceService deviceService;
    private MyPropertiesConfig myPropertiesConfig;

    @Value("${my.pagesize}")
    int pagesize;


    @Autowired
    public ShortPostController(ShortPostDAO postDAO, DeviceDAO deviceDAO, ShortPostService postService, DeviceService deviceService, MyPropertiesConfig myPropertiesConfig) {
        this.postDAO = postDAO;
        this.deviceDAO = deviceDAO;
        this.postService = postService;
        this.deviceService = deviceService;
        this.myPropertiesConfig = myPropertiesConfig;
    }


    @GetMapping("/devices/{inv}/{page}")
    public String getAllForDevice(@PathVariable String inv, Model model, @PathVariable int page) {

        DeviceDTO dto = DeviceMapper.map(deviceDAO.findByInventNumber(inv));
        List<ShortPost> all = postDAO.findAllByDevice_InventNumber(inv);
        Map<Integer, ShortPostDTO> mapa = postService.findAllByDevice(inv, page - 1, pagesize);



        int amount = postService.numberByDevice(inv);


        int numberOfPages = (postService.numberByDevice(inv)) / pagesize + 1;

        if (postService.numberByDevice(inv) % pagesize == 0) {
            numberOfPages--;
        }

        List<Integer> morePages = new LinkedList<>();
        int i = 2;
        int lastPage = 1;

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
        dto.setDate(CalendarUtil.cal2string(Calendar.getInstance() ));
        model.addAttribute("newPost", dto);
        model.addAttribute("authors", ListOfEnumValues.authors);
        Map<String, String> mapa = new HashMap<>();
        List<String> keys = deviceDAO.findAll().stream().map(d -> d.getInventNumber()).collect(Collectors.toList());
        for (String key : keys) {
            Device device = deviceDAO.findByInventNumber(key);
            mapa.put(key, device.getDeviceDescription());
        }
        model.addAttribute("devices", mapa);
        System.out.println(System.getProperty("java.io.tmpdir"));
        return "posts/addPostForm";
    }

    @GetMapping("/addForm/{inventNumber}")
    public String addForm(Model model, @PathVariable String inventNumber) {
        ShortPostDTO dto = new ShortPostDTO();
        dto.setDate(CalendarUtil.cal2string(Calendar.getInstance() ));
        model.addAttribute("shortPostDTO", dto);
        model.addAttribute("authors", ListOfEnumValues.authors);
        model.addAttribute("inventNumber", inventNumber);

        String text = deviceDAO.findByInventNumber(inventNumber).getDeviceDescription()
                + " " + deviceDAO.findByInventNumber(inventNumber).getRoom();
        model.addAttribute("text", text);

        return "posts/addPostFormInv";
    }

    @GetMapping("/all/{year}/{page}")
    public String allByYear(Model model, @PathVariable int year, @PathVariable int page) {
        Map<Integer, ShortPostDTO> mapa = postService.findAll(year, page - 1, 10);
        int numberOfPages = (postService.numberByYear(year)) / 10 + 1;

        if (postService.numberByYear(year) % 10 == 0) {
            numberOfPages--;
        }
        List<Integer> morePages = new LinkedList<>();
        List<ShortPost> allPosts = postService.findAllByYear(year);

        int amount = postService.numberByYear(year);

        for (ShortPost post : allPosts) {
            if (post.getContent().contains("kliknięciu")) {
                amount--;
            }
        }

        int i = 2;
        int lastPage = 1;

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
        model.addAttribute("newPost", dto);
        model.addAttribute("authors", ListOfEnumValues.authors);
        Map<String, String> mapa = new HashMap<>();
        List<String> keys = deviceDAO.findAll().stream().map(d -> d.getInventNumber()).collect(Collectors.toList());
        for (String key : keys) {
            Device device = deviceDAO.findByInventNumber(key);
            mapa.put(key, device.getDeviceDescription());
        }
        model.addAttribute("devices", mapa);
        System.out.println(System.getProperty("java.io.tmpdir"));
        return "posts/addPostForm";
    }

    public String addFormErr(Model model, @PathVariable String inventNumber, ShortPostDTO dto) {
        model.addAttribute("shortPostDTO", dto);
        model.addAttribute("authors", ListOfEnumValues.authors);
        model.addAttribute("inventNumber", inventNumber);

        String text = deviceDAO.findByInventNumber(inventNumber).getDeviceDescription()
                + " " + deviceDAO.findByInventNumber(inventNumber).getRoom();
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
        model.addAttribute("newPost", dto);
        model.addAttribute("authors", ListOfEnumValues.authors);
        Map<String, String> mapa = new HashMap<>();
        List<String> keys = deviceDAO.findAll().stream().map(d -> d.getInventNumber()).collect(Collectors.toList());
        for (String key : keys) {
            Device device = deviceDAO.findByInventNumber(key);
            mapa.put(key, device.getDeviceDescription());
        }
        model.addAttribute("devices", mapa);
        model.addAttribute("id", id);
        return "posts/editPostForm";
    }

    @GetMapping("/removeSystem")
    public String removeSystem(){
        List<ShortPost> posts = postDAO.findAll();
        for (ShortPost post: posts) {
            if (post.getContent().contains("SYSTEM")) postDAO.delete(post);
        }

        ShortPostDTO dto = new ShortPostDTO();
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
        if (bindingResult.hasFieldErrors()) {
            List<FieldError> allErrors;
            allErrors = bindingResult.getFieldErrors();

            model.addAttribute("bindingResult", bindingResult);
            model.addAttribute("errors", allErrors);
            model.addAttribute("errorsAmount", allErrors.size());
            return editFormErr(model, shortPostDTO.getInventNumber(), shortPostDTO, id);
        }


        ShortPost post = postDAO.findByPostId(id);
        post.setAuthor(shortPostDTO.getAuthor());
        post.setContent(shortPostDTO.getContent());
        post.setPostDate(CalendarUtil.string2cal(shortPostDTO.getDate()));
        post.setDevice(deviceDAO.findByInventNumber(shortPostDTO.getInventNumber()));
        Calendar calendar= CalendarUtil.string2cal(shortPostDTO.getDate());
        post.setDate(calendar.getTime());
        postDAO.save(post);
        return "redirect:/dtnetwork";
    }

    @GetMapping("/modalPosts/")
    public String modalPosts (Model model, @ModelAttribute("posts") int year ){

        return allByYear(model,year,1);
    }

    public String editFormErr(Model model, String inventNumber, ShortPostDTO dto, Integer id) {
        model.addAttribute("newPost", dto);
        model.addAttribute("authors", ListOfEnumValues.authors);
        Map<String, String> mapa = new HashMap<>();
        List<String> keys = deviceDAO.findAll().stream().map(d -> d.getInventNumber()).collect(Collectors.toList());
        for (String key : keys) {
            Device device = deviceDAO.findByInventNumber(key);
            mapa.put(key, device.getDeviceDescription());
        }
        model.addAttribute("devices", mapa);
        model.addAttribute("id", id);
        return "posts/editPostForm";
    }


}
