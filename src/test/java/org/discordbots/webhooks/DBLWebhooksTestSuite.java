package org.discordbots.webhooks;

import org.discordbots.webhooks.dropwizard.DBLDropwizardWebhooksTest;
import org.discordbots.webhooks.eclipsejetty.DBLEclipseJettyWebhooksTest;
import org.discordbots.webhooks.springboot.DBLSpringBootWebhooksTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
  DBLDropwizardWebhooksTest.class,
  DBLEclipseJettyWebhooksTest.class,
  DBLSpringBootWebhooksTest.class
})
public class DBLWebhooksTestSuite {}
