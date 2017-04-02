package bernardjason.weblogic.demo.demojar

import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.Arrays
import java.util.Properties
import java.util.Scanner

import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.gmail.Gmail
import com.google.api.services.gmail.GmailScopes
import com.google.api.services.gmail.model.Message

import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

object GmailSend {

  val APPLICATION_NAME = "Gmail API Java Quickstart";

	val DATA_STORE_DIR = new java.io.File( System.getProperty("user.home"), ".credentials/gmail-java-quickstart");
	val CLIENT_SECRET = new java.io.File( System.getProperty("user.home"), ".credentials/client_secret.json");
	val SCOPES = Arrays.asList(GmailScopes.GMAIL_SEND);
	
	
	val  JSON_FACTORY = JacksonFactory .getDefaultInstance();
	val		HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
	val		DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);

	def authorize() = {
  	val in = new FileInputStream( CLIENT_SECRET );
		
		val clientSecrets = GoogleClientSecrets.load( JSON_FACTORY, new InputStreamReader(in));

		val flow = new GoogleAuthorizationCodeFlow.Builder( HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
				.setDataStoreFactory(DATA_STORE_FACTORY)
				.setAccessType("offline").build();

		val credential = new AuthorizationCodeInstalledApp(flow,
				new LocalServerReceiver()).authorize("user");

		credential;
	}


	def getGmailService() = {
		val credential = authorize();
		new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
				.setApplicationName(APPLICATION_NAME).build();
	}

	def main(args:Array[String]):Unit = {
		val service = getGmailService();

		val scanner = new Scanner(System.in);

		System.out.print("From email: ");
		val from = scanner.next();
		System.out.print("To email: ");
		val to = scanner.next();

		val message = createMessageWithEmail(createEmail(to, from, "test",
				"hello world"));

		service.users().messages().send(from, message).execute();

	}

	def createMessageWithEmail(emailContent:MimeMessage ) = {
		val buffer = new ByteArrayOutputStream();
		emailContent.writeTo(buffer);
		val bytes = buffer.toByteArray();
		val encodedEmail = Base64.encodeBase64URLSafeString(bytes);
		val message = new Message();
		message.setRaw(encodedEmail);
		message;
	}

	def createEmail(to:String , from:String, subject:String, bodyText:String)= {
		val props = new Properties();
		val session = javax.mail.Session.getDefaultInstance( props, null);

		val email = new MimeMessage(session);

		email.setFrom(new InternetAddress(from));
		email.addRecipient(javax.mail.Message.RecipientType.TO,
				new InternetAddress(to));
		email.setSubject(subject);
		email.setText(bodyText);
		email;
	}

}