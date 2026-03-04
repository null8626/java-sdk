package org.discordbots.webhooks;

import org.discordbots.webhooks.dropwizard.DBLDropwizardWebhooksTest;
import org.discordbots.webhooks.eclipsejetty.DBLEclipseJettyWebhooksTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({DBLDropwizardWebhooksTest.class, DBLEclipseJettyWebhooksTest.class})
public class DBLWebhooksTestSuite {}
