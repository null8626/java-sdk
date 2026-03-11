package gg.top.webhooks;

import gg.top.webhooks.dropwizard.TopggDropwizardWebhookTests;
import gg.top.webhooks.eclipsejetty.TopggEclipseJettyWebhookTests;
import gg.top.webhooks.springboot.TopggSpringBootWebhookTests;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
  TopggDropwizardWebhookTests.class,
  TopggEclipseJettyWebhookTests.class,
  TopggSpringBootWebhookTests.class
})
public class TopggWebhookTestSuite {}
