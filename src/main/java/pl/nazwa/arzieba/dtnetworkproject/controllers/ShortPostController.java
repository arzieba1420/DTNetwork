package pl.nazwa.arzieba.dtnetworkproject.controllers;

import org.springframework.beans.factory.annotation.Value;
import pl.nazwa.arzieba.dtnetworkproject.configuration.MyPropertiesConfig;
import pl.nazwa.arzieba.dtnetworkproject.dao.DeviceDAO;
import pl.nazwa.arzieba.dtnetworkproject.dao.ShortPostDAO;
import pl.nazwa.arzieba.dtnetworkproject.dto.DeviceDTO;
import pl.nazwa.arzieba.dtnetworkproject.dto.ShortPostDTO;
import pl.nazwa.arzieba.dtnetworkproject.model.Device;
import pl.nazwa.arzieba.dtnetworkproject.model.ShortPost;
import pl.nazwa.arzieba.dtnetworkproject.services.device.DeviceService;
import pl.nazwa.arzieba.dtnetworkproject.services.shortPost.ShortPostService;
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
            System.out.println(allErrors.size());

            model.addAttribute("bindingResult", result);
            model.addAttribute("errors", allErrors);

            model.addAttribute("errorsAmount", allErrors.size());
            return addFormErr(model, dto);
        }
        postService.create(dto);
        return "redirect:/dtnetwork";
    }

    @PostMapping("/addAsModel/stay")
    public String create3(Model model, @Valid @ModelAttribute ShortPostDTO shortPostDTO, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasFieldErrors()) {
            System.out.println(model.asMap());
            List<FieldError> allErrors;
            allErrors = bindingResult.getFieldErrors();
            System.out.println(allErrors.size());

            model.addAttribute("bindingResult", bindingResult);
            model.addAttribute("errors", allErrors);
            model.addAttribute("errorsAmount", allErrors.size());
            return addFormErr(model, shortPostDTO.getInventNumber(), shortPostDTO);
        }
        postService.create(shortPostDTO);
        return "redirect:/posts/devices/" + shortPostDTO.getInventNumber();
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
        model.addAttribute("newPost", new ShortPostDTO());
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
        model.addAttribute("shortPostDTO", new ShortPostDTO());
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
}

/*    @PostMapping("/search")
    public String search(Model model, @ModelAttribute)

}*/
