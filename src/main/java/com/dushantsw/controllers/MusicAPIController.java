package com.dushantsw.controllers;

import com.dushantsw.integration.entities.Artist;
import com.dushantsw.integration.managers.exceptions.AboutRetrievingException;
import com.dushantsw.integration.managers.exceptions.InvalidMBIDException;
import com.dushantsw.integration.managers.exceptions.MusicBrainzException;
import com.dushantsw.services.MusicAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.ServiceUnavailableException;
import javax.websocket.server.PathParam;

/**
 * <code>MusicAPIController</code>
 *
 * @author dushantsw
 */
@RestController
public class MusicAPIController {
    private MusicAPIService service;

    public MusicAPIController(@Autowired MusicAPIService service) {
        this.service = service;
    }

    @RequestMapping(value = "/artist/{mbId}", method = RequestMethod.GET)
    public Artist artistByMBId(@PathVariable String mbId) {
        try {
            return service.getArtistByMBId(mbId);
        } catch (InvalidMBIDException | MusicBrainzException | AboutRetrievingException | ServiceUnavailableException e) {
            e.printStackTrace();
        }

        return null;
    }
}
