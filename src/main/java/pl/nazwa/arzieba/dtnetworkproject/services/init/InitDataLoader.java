package pl.nazwa.arzieba.dtnetworkproject.services.init;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.nazwa.arzieba.dtnetworkproject.dao.DeviceDAO;
import pl.nazwa.arzieba.dtnetworkproject.dto.IssueDocumentDTO;
import pl.nazwa.arzieba.dtnetworkproject.dto.ShortPostDTO;
import pl.nazwa.arzieba.dtnetworkproject.model.Author;
import pl.nazwa.arzieba.dtnetworkproject.model.PostLevel;
import pl.nazwa.arzieba.dtnetworkproject.services.issueDocument.IssueDocService;
import pl.nazwa.arzieba.dtnetworkproject.services.shortPost.ShortPostService;
import pl.nazwa.arzieba.dtnetworkproject.utils.mail.EmailConfiguration;

import java.util.stream.IntStream;

@Component
public class InitDataLoader {

    DeviceDAO deviceDAO;
    IssueDocService issueDocService;
    ShortPostService shortPostService;
    EmailConfiguration emailConfiguration;
    int range = 20;


    public InitDataLoader() {}

    @Autowired
    public InitDataLoader(DeviceDAO deviceDAO, IssueDocService issueDocService, ShortPostService shortPostService) {
        this.deviceDAO = deviceDAO;
        this.issueDocService = issueDocService;
        this.shortPostService = shortPostService;
    }


    @EventListener(ApplicationReadyEvent.class)
    public void onInit() {
        IntStream.range(1, range).forEach(i -> {
            String inventNumber = deviceDAO.findAll().get(i - 1).getInventNumber();
            IssueDocumentDTO issueDocumentDTO = new IssueDocumentDTO("ACK-Sign_" + i, "DelivCompany", "123-456-78-90", "202"+i%2+"-04-"+i , "Issue title_" + i,
                    "Sample issue document details_" + i, inventNumber, (double) i * 25);
            issueDocService.create(issueDocumentDTO);

            ShortPostDTO shortPostDTO1 = new ShortPostDTO(Author.USER, "Sample GENERAL short post content_" + i,"202"+i%2+"-04-"+i , inventNumber, false, PostLevel.GENERAL);
            ShortPostDTO shortPostDTO2 = new ShortPostDTO(Author.DTN, "Sample GENERAL short post content_" + i,"202"+i%2+"-04-"+i  , inventNumber, false, PostLevel.GENERAL);
            shortPostService.createOnInit(shortPostDTO1);
            shortPostService.createOnInit(shortPostDTO2);

        });
        ShortPostDTO shortPostDTO1 = new ShortPostDTO(Author.DTN, "Sample INFO short post content [SYSTEM]", "2022-04-11", "DTN", false, PostLevel.INFO);
        ShortPostDTO shortPostDTO2 = new ShortPostDTO(Author.DTN, "Sample POWER short post content [SYSTEM]", "2022-04-11", "DTN", false, PostLevel.POWER);
        ShortPostDTO shortPostDTO3 = new ShortPostDTO(Author.DTN, "Sample DAMAGE short post content [SYSTEM]", "2022-04-11", "DTN", false, PostLevel.DAMAGE);
        ShortPostDTO shortPostDTO4 = new ShortPostDTO(Author.DTN, "Sample UPDATE short post content [SYSTEM]", "2022-04-11", "DTN", false, PostLevel.UPDATE);
        shortPostService.createOnInit(shortPostDTO1);
        shortPostService.createOnInit(shortPostDTO2);
        shortPostService.createOnInit(shortPostDTO3);
        shortPostService.createOnInit(shortPostDTO4);
    }
    }

