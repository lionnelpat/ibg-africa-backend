package org.forbidec.web.rest;

import org.forbidec.service.DashboardService;
import org.forbidec.service.dto.dashboard.DashboardDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Reporting global (effectifs, réussite, évolution, mentions, dernières sessions).
 */
@RestController
@RequestMapping("/api")
public class DashboardResource {

    private static final Logger LOG = LoggerFactory.getLogger(DashboardResource.class);

    private final DashboardService dashboardService;

    public DashboardResource(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/dashboard")
    public DashboardDTO getDashboard() {
        LOG.debug("REST request to get the global Dashboard");
        return dashboardService.getDashboard();
    }
}
