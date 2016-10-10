package co.grandcircus.movies.controller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import co.grandcircus.movies.rest.SunService;


@Controller
public class SunController {
	
	private static final Logger logger = LoggerFactory.getLogger(SunController.class);
	
	@Autowired
	private SunService sunService;
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping("/sun")
	public String home(Locale locale, Model model) {
		// add the 'weather' variable to the JSP
		model.addAttribute("sun", sunService.getCurrentSunStatsGrandCircus());
		
		logger.info("/sun -> sun.jsp");
		// select to use the weather.jsp view
		return "sun";
	}
	
}