package za.co.las.stock.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;

import com.adobe.pdf.PDFDocument;
import com.adobe.pdf.PDFFactory;

import flex.acrobat.pdf.XFAHelper;
import za.co.las.stock.object.Accessory;
import za.co.las.stock.object.InstallationLocation;
import za.co.las.stock.object.Quotation;
import za.co.las.stock.object.Stock;
import za.co.las.stock.object.StockImage;
import za.co.las.stock.object.User;

public class DocumentService {

	private byte[] getQuoteFromQuoteUserAndStockImageUsingXPAAJ(Quotation quote, User user, StockImage stockImage) {
		byte[] documentBytes = null;
		InputStream quotationPDFInput = null;
		InputStream formInputStream = null;
		InputStream savedPDFStream = null;
		try {
			String formData = buildXMLDataForForm(quote, user);
			
			//get the source XML stream from the template...
			if (quote.getCompanyId() == 1) {
				quotationPDFInput = DocumentService.class.getResourceAsStream("../../../../../pdf/LiftAndShiftQuotationForm_V16.pdf");
			}
			else {
				quotationPDFInput = DocumentService.class.getResourceAsStream("../../../../../pdf/BowmanCranesQuotationForm_V16.pdf");
			}
			PDFDocument mergePdfDocument = PDFFactory.openDocument(quotationPDFInput);
				
			//check to make sure everything is ok...
			System.err.println(formData);
					
			//now need to pass the dom back to the PDF Document so that we can merge it...
			byte[] inputData = formData.getBytes("UTF8");
			formInputStream = new ByteArrayInputStream(inputData);
			mergePdfDocument.importFormData(formInputStream);
					
			//now need to get the byte[] and write this to the Document Table...
			savedPDFStream = mergePdfDocument.save();
			documentBytes = IOUtils.toByteArray(savedPDFStream);
			
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
		finally {
			//close the stream...
			try {
				quotationPDFInput.close();
				formInputStream.close();
				savedPDFStream.close();
			}
			catch (IOException ioe) {}
		}
		return documentBytes;
	}
	
	public byte[] getQuoteFromQuoteUserAndStockImage(Quotation quote, User user) {
		byte[] documentBytes = null;
		InputStream quotationPDFInput = null;
		
		String formData = buildXMLDataForForm(quote, user);
		
		if (quote.getCompanyId() == 1) {
			quotationPDFInput = DocumentService.class.getResourceAsStream("../../../../../pdf/LiftAndShiftQuotationForm_V18.pdf");
		}
		else {
			quotationPDFInput = DocumentService.class.getResourceAsStream("../../../../../pdf/BowmanCranesQuotationForm_V18.pdf");
		}
		
		//System.out.println(formData);
		
		XFAHelper helper = new XFAHelper();
		try {
			helper.open(quotationPDFInput);
		
			ByteArrayInputStream bais = new	ByteArrayInputStream(formData.getBytes("UTF-8"));
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			Document dataset = builder.parse(bais);
			helper.importDataset(dataset);
			documentBytes = helper.saveToByteArray();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return documentBytes;
	}
	
	private String buildXMLDataForForm(Quotation quote, User user) {
		String formData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
						"<form1>"+
						    "<subQuoteHeader>"+
						        "<txtQuotationType>"+quote.getQuotationLineItems().get(0).getStockModel()+"</txtQuotationType>"+
						        "<txtShowItemPrices>"+quote.getShowLineItemPrices()+"</txtShowItemPrices>"+
						    "</subQuoteHeader>"+
						    "<subCustomerInfo>"+
						    "    <txtDate>"+quote.getQuotationDate()+"</txtDate>"+
						    "    <txtCompanyName>"+quote.getCustomer().getName()+"</txtCompanyName>"+
						    "    <txtAdress>"+quote.getCustomer().getAddress()+"</txtAdress>"+
						    "    <txtEmail>"+quote.getCustomer().getEmailAddress()+"</txtEmail>"+
						    "    <txtPhoneNumber>"+quote.getCustomer().getPhoneNumber()+"</txtPhoneNumber>"+
						    "    <txtAttention>"+quote.getCustomer().getAttention()+"</txtAttention>"+
						    "    <txtRefNumber>"+quote.getQuotationId()+"</txtRefNumber>"+
						    "</subCustomerInfo>"+
						    "<subSalutation/>"+
						    "<subTechnicalDetails>"+
						    "    <txtOffer>"+
						    quote.getQuotationLineItems().get(0).getStockDescription()+
						    "    </txtOffer>"+
						    "    <txtTechnicalSpecs>"+
						    "        <body xmlns=\"http://www.w3.org/1999/xhtml\">"+
						    quote.getQuotationLineItems().get(0).getTechnicalSpecs()+
						    "        </body>"+
						    "    </txtTechnicalSpecs>"+
						    "</subTechnicalDetails>"+
						    buildQuotationItemsSection(quote.getQuotationLineItems(),quote.getAccessoryItems(), quote.getInstallLocation())+
						    "<subQuotationOptionalExtras>"+
						    //buildAccessoryItems(quote.getAccessoryItems(), quote.getInstallLocation())+
						    "</subQuotationOptionalExtras>"+
						    "<subNotes>"+
						    "    <txtNotes>"+
						    "        <body xmlns=\"http://www.w3.org/1999/xhtml\">"+
						    quote.getNotes()+
						    "        </body>"+
						    "    </txtNotes>"+
						    "</subNotes>"+
						    "<subDelivery>"+
						    "    <txtDelivery>"+
						    "        <body xmlns=\"http://www.w3.org/1999/xhtml\">"+
						    quote.getDelivery()+
						    "        </body>"+
						    "    </txtDelivery>"+
						    "</subDelivery>"+
						    "<subInstallation>"+
						    "    <txtInstallation>"+
						    "        <body xmlns=\"http://www.w3.org/1999/xhtml\">"+
						    quote.getInstallation()+
						    "        </body>"+
						    "    </txtInstallation>"+
						    "</subInstallation>"+
						    "<subWarranty>"+
						    "    <txtWarranty>"+
						    "        <body xmlns=\"http://www.w3.org/1999/xhtml\">"+
						    quote.getWarranty()+
						    "        </body>"+
						    "    </txtWarranty>"+
						    "</subWarranty>"+
						    "<subPriceVariation>"+
						    "    <txtPriceVariation>"+
						    "        <body xmlns=\"http://www.w3.org/1999/xhtml\">"+
						    buildVariation(quote.getVariation(), quote.getRate())+
						    "        </body>"+
						    "    </txtPriceVariation>"+
						    "</subPriceVariation>"+
						    "<subQuotationValidity>"+
						    "    <txtPriceVariation>"+
						    "        <body xmlns=\"http://www.w3.org/1999/xhtml\">"+
						    quote.getValidity()+
						    "        </body>"+
						    "    </txtPriceVariation>"+
						    "</subQuotationValidity>"+
						    "<subThanks>"+
						    "    <txtUserName>"+user.getDisplayName()+"</txtUserName>"+
						    "    <txtUserContact>"+user.getContactNumber()+"</txtUserContact>"+
						    "    <txtUserEmail>"+user.getEmailAddress()+"</txtUserEmail>"+
						    "</subThanks>"+
						"</form1>";
                    		
        return formData;
	}
	
	private String buildVariation(String orig, String rate) {
		return orig.replace("<<rate>>", rate);
	}
	
	private String buildQuotationItemsSection(ArrayList<Stock> stockItems, ArrayList<Accessory> accessoryItems, InstallationLocation location) {
		DecimalFormat df = new DecimalFormat("0.00");
		double subTotalAmount = 0;
		double vatAmount = 0;
		String stockLineItems = "<subQuotationDetails>"+
						    "    <subHeading/>"+
						    "    <subTable>";
		
		for (Stock s:stockItems) {
			String lineItem = "        <subLineItem>"+
						      "            <txtLineItemDescription>"+s.getStockModel()+" "+s.getStockSeries()+"</txtLineItemDescription>"+
						      "            <txtLineItemAmount>"+s.getPricing()+"</txtLineItemAmount>"+
						      "        </subLineItem>";
			stockLineItems += lineItem;
			subTotalAmount += s.getPricing();
		}
		
		if (location != null &&
			location.getLocation().length() > 0) {
			stockLineItems +="        <subLineItem>"+
						     "            <txtLineItemDescription>Installation - "+location.getLocation()+"</txtLineItemDescription>"+
						     "            <txtLineItemAmount>"+location.getPrice()+"</txtLineItemAmount>"+
						     "        </subLineItem>";
			subTotalAmount += location.getPrice();
		}
		for (Accessory acc:accessoryItems) {
			stockLineItems +="        <subLineItem>"+
						     "            <txtLineItemDescription>"+acc.getAccessoryDescription()+"</txtLineItemDescription>"+
						     "            <txtLineItemAmount>"+acc.getPricing()+"</txtLineItemAmount>"+
						     "        </subLineItem>";
			subTotalAmount += acc.getPricing();
		}
		
		//add the line items for the optional extras...
		
		String subTotalAmountStr = new Double(subTotalAmount).toString();
		vatAmount = subTotalAmount * 0.14;
		String vatAmountStr = df.format(vatAmount);
		String totalAmountStr = df.format(subTotalAmount + vatAmount);
		
		stockLineItems +=   "    </subTable>"+
						    "    <subSubTotal>"+
						    "        <txtSubTotal>"+subTotalAmountStr+"</txtSubTotal>"+
						    "    </subSubTotal>"+
						    "    <subVat>"+
						    "        <txtSubTotal>"+vatAmountStr+"</txtSubTotal>"+
						    "    </subVat>"+
						    "    <subTotal>"+
						    "        <txtSubTotal>"+totalAmountStr+"</txtSubTotal>"+
						    "    </subTotal>"+
						    "</subQuotationDetails>";
		return stockLineItems;
	}
	
	private String buildAccessoryItems(ArrayList<Accessory> accessoryItems, InstallationLocation location) {
		String oeLineItems = "    <subHeading/>"+
						    "    <subTable>";
		if (location.getLocation().length() > 0) {
			oeLineItems +=	"        <subLineItem>"+
						    "            <txtLineItemDescription>Installation - "+location.getLocation()+"</txtLineItemDescription>"+
						    "            <txtLineItemAmount>"+location.getPrice()+"</txtLineItemAmount>"+
						    "        </subLineItem>";
		}
		for (Accessory acc:accessoryItems) {
			oeLineItems +=	"        <subLineItem>"+
						    "            <txtLineItemDescription>"+acc.getAccessoryDescription()+"</txtLineItemDescription>"+
						    "            <txtLineItemAmount>"+acc.getPricing()+"</txtLineItemAmount>"+
						    "        </subLineItem>";
		}
		oeLineItems += "    </subTable>";
		return oeLineItems;
	}
	
	private String getBase64StringFromImageForHTML(byte[] imageData) {
		return Base64.encodeBase64String(imageData);
	}
}
