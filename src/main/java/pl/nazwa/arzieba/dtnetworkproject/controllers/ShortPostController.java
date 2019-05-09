package pl.nazwa.arzieba.dtnetworkproject.controllers;

import pl.nazwa.arzieba.dtnetworkproject.DtNetworkApplication;
import pl.nazwa.arzieba.dtnetworkproject.dao.DeviceDAO;
import pl.nazwa.arzieba.dtnetworkproject.dao.ShortPostDAO;
import pl.nazwa.arzieba.dtnetworkproject.dto.DeviceDTO;
import pl.nazwa.arzieba.dtnetworkproject.dto.ShortPostDTO;
import pl.nazwa.arzieba.dtnetworkproject.model.Device;
import pl.nazwa.arzieba.dtnetworkproject.model.ShortPost;
import pl.nazwa.arzieba.dtnetworkproject.services.device.DeviceService;
import pl.nazwa.arzieba.dtnetworkproject.services.shortPost.ShortPostService;
import pl.nazwa.arzieba.dtnetworkproject.utils.enums.ListOfEnumValues;
import pl.nazwa.arzieba.dtnetworkproject.utils.shortPost.ShortPostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
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




    @Autowired
    public ShortPostController(ShortPostDAO postDAO, DeviceDAO deviceDAO, ShortPostService postService, DeviceService deviceService) {
        this.postDAO = postDAO;
        this.deviceDAO = deviceDAO;
        this.postService = postService;
        this.deviceService=deviceService;

    }




    @GetMapping("/devices/{inv}")
    public String getAllForDevice(@PathVariable String inv, Model model){
       List<ShortPost> shortPosts= postDAO.findAllByDevice_InventNumberOrderByDateDesc(inv);
        DeviceDTO dto= deviceService.findByInventNumber(inv);
        Map<Integer,ShortPostDTO> mapa = new LinkedHashMap<>();

        for (ShortPost post: shortPosts) {
            mapa.put(post.getPostId(), ShortPostMapper.map(post));
        }

         List<ShortPostDTO> list= postService.findByDevice(inv);

        model.addAttribute("amount",shortPosts.size());
         model.addAttribute("posts",mapa);
         model.addAttribute("dto",dto);
        return "posts/allPostsForDevice";
    }


    @PostMapping("/add")
    public String  create(Model model, ShortPostDTO dto){
        postService.create(dto);
        return "error";
    }

    @PostMapping("/addAsModel")
    public String  create2(Model model,  @Valid @ModelAttribute("dto")   ShortPostDTO dto, BindingResult result, HttpServletRequest request){

        if(result.hasFieldErrors()){
           List<FieldError> allErrors;
           allErrors = result.getFieldErrors();
            System.out.println(allErrors.size());

            model.addAttribute("bindingResult", result);
            model.addAttribute("errors",allErrors);

            model.addAttribute("errorsAmount",allErrors.size());
            return addForm(model);
        }
        postService.create(dto);
        return "redirect:/dtnetwork";
    }

    @PostMapping("/addAsModel/stay")
    public String  create3(Model model,@Valid @ModelAttribute  ShortPostDTO shortPostDTO, BindingResult bindingResult, HttpServletRequest request){
        if(bindingResult.hasFieldErrors()){
            System.out.println(model.asMap());
            List<FieldError> allErrors;
            allErrors = bindingResult.getFieldErrors();
            System.out.println(allErrors.size());

            model.addAttribute("bindingResult", bindingResult);
            model.addAttribute("errors",allErrors);
            model.addAttribute("errorsAmount",allErrors.size());
            return addForm(model,shortPostDTO.getInventNumber());
        }
        postService.create(shortPostDTO);
        return "redirect:/posts/devices/"+shortPostDTO.getInventNumber();
    }

    @GetMapping("/delete/{id}")
    public String remove (@PathVariable Integer id, Model model){

        postService.remove(id);
        return "redirect:/dtnetwork";
    }

    @GetMapping("/delete/{id}/stay")
    public String removeAndStay (@PathVariable Integer id, Model model, HttpServletRequest request){

        postService.remove(id);
        return "redirect:"+request.getHeader("Referer") ;
    }

    @GetMapping("/addForm")
    public String addForm(Model model){
        model.addAttribute("newPost",new ShortPostDTO());
        model.addAttribute("authors", ListOfEnumValues.authors);
        Map<String,String> mapa = new HashMap<>();
        List<String> keys = deviceDAO.findAll().stream().map(d->d.getInventNumber()).collect(Collectors.toList());
        for (String key:keys) {
            Device device = deviceDAO.findByInventNumber(key);
            mapa.put(key,device.getDeviceDescription());
        }
        model.addAttribute("devices",mapa);
        System.out.println(System.getProperty("java.io.tmpdir"));
        return "posts/addPostForm";
    }

    @GetMapping("/addForm/{inventNumber}")
    public String addForm(Model model, @PathVariable String inventNumber){
        model.addAttribute("shortPostDTO",new ShortPostDTO());
        model.addAttribute("authors", ListOfEnumValues.authors);
        model.addAttribute("inventNumber", inventNumber);

        String text = deviceDAO.findByInventNumber(inventNumber).getDeviceDescription()
                +" "+deviceDAO.findByInventNumber(inventNumber).getRoom();
        model.addAttribute("text",text);
        return "posts/addPostFormInv";
    }

    @GetMapping("/all/{year}/{page}")
    public String allByYear(Model model, @PathVariable int year, @PathVariable int page){
       Map<Integer,ShortPostDTO> mapa = postService.findAll(year,page-1,10);
       int numberOfPages = (postService.numberByYear(year))/10 + 1;

       if(postService.numberByYear(year)%10==0){
           numberOfPages--;
       }
       List<Integer> morePages = new LinkedList<>();

       int i = 2;
       int lastPage = 1;

       while(i<=numberOfPages){
           morePages.add(i);
           i++;
           lastPage++;
       }

       model.addAttribute("amount",postService.numberByYear(year));
       model.addAttribute("classActiveSettings","active");
       model.addAttribute("posts",mapa);
       model.addAttribute("pages",morePages);
       model.addAttribute("currentPage",page);
       model.addAttribute("year",year);
       model.addAttribute("lastPage",lastPage);
       return "posts/allByYear";
    }


}
