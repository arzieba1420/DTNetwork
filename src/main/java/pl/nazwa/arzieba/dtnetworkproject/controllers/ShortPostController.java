package pl.nazwa.arzieba.dtnetworkproject.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.nazwa.arzieba.dtnetworkproject.dto.ShortPostDTO;
import pl.nazwa.arzieba.dtnetworkproject.services.shortPost.ShortPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping("/posts")
public class ShortPostController implements WebMvcConfigurer {

    //--------------------------------------------------------------------LOCAL VARIABLES---------------------------------------------------------------------------------------

    Logger logger = LoggerFactory.getLogger(this.getClass());
    private ShortPostService postService;

    //--------------------------------------------------------------------CONSTRUCTOR---------------------------------------------------------------------------------------

    @Autowired
    public ShortPostController( ShortPostService postService) {
        this.postService = postService;
    }

    //--------------------------------------------------------------------BUSINESS LOGIC---------------------------------------------------------------------------------------

    @GetMapping("/devices/{inv}/{page}")
    public String getAllPostsForDevice(@PathVariable String inv, Model model, @PathVariable int page) {
        return postService.getAllPostsForDevice(inv, model, page);
    }

    @PostMapping("/add")
    public String create(Model model, ShortPostDTO dto) {
        postService.create(dto);
        return "error";
    }

    @PostMapping("/addAsModel")
    public String createShortPost(Model model, @Valid @ModelAttribute("newPost") ShortPostDTO shortPostDTO, BindingResult result, HttpServletRequest request) {
        return postService.createShortPost(model, shortPostDTO, result, request);
    }

    @PostMapping("/addAsModel/stay")
    public String createShortPostAndStay( @Valid @ModelAttribute ShortPostDTO shortPostDTO, BindingResult bindingResult,Model model, HttpServletRequest request) {
        return postService.createShortPostAndStay(shortPostDTO, bindingResult, model, request);
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
        return postService.addForm(model);
    }

    @GetMapping("/addForm/{inventNumber}")
    public String addPostFormForDevice(Model model, @PathVariable String inventNumber) {
        return postService.addPostFormForDevice(model, inventNumber);
    }

    @GetMapping("/all/{year}/{page}")
    public String allPostsByYear(Model model, @PathVariable int year, @PathVariable int page) {
        return postService.allPostsByYear(model, year, page);
    }

    @GetMapping("/search")
    public String searchByPhrase(Model model, @ModelAttribute("search") String phrase ){
        return postService.searchByPhrase(model, phrase);
    }

    @GetMapping("/edit/{id}")
    public String editPostForm(Model model, @PathVariable Integer id){
        return postService.editPostForm(model, id);
    }

    @GetMapping("/removeSystem")
    public String removeSystemPosts(){
        return postService.removeSystemPosts();
    }

    @PostMapping("/edit/{id}")
    public String saveEditedPost( @Valid @ModelAttribute("newPost") ShortPostDTO shortPostDTO,BindingResult bindingResult, Model model, @PathVariable Integer id ){
        return postService.saveEditedPost(shortPostDTO, bindingResult, model, id);
    }

    @GetMapping("/modalPosts/")
    public String modalPosts (Model model, @ModelAttribute("posts") int year ){
        return allPostsByYear(model,year,1);
    }
}
