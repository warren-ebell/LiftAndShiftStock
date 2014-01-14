package za.co.las.stock.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;

import za.co.las.stock.object.OptionalExtra;
import za.co.las.stock.object.Quotation;
import za.co.las.stock.object.Stock;
import za.co.las.stock.object.User;

import com.adobe.pdf.PDFDocument;
import com.adobe.pdf.PDFFactory;

public class DocumentService {
	public byte[] getQuotePDFFromQuote(Quotation quote, User user) {
		byte[] documentBytes = null;
		InputStream quotationPDFInput = null;
		InputStream formInputStream = null;
		InputStream savedPDFStream = null;
		try {
			String formData = buildXMLDataForForm(quote, user);
		
			//get the source XML stream from the template...
			quotationPDFInput = DocumentService.class.getResourceAsStream("../../../../../pdf/LiftAndShiftQuotationForm.pdf");
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
	
	private String buildXMLDataForForm(Quotation quote, User user) {
		String formData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
						"<form1>"+
						    "<subQuoteHeader>"+
						        "<txtQuotationType>"+quote.getQuotationLineItems().get(0).getStockModel()+"</txtQuotationType>"+
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
						    "    <ImageField1/>"+
						    "    <txtTechnicalSpecs>"+
						    "        <body xmlns=\"http://www.w3.org/1999/xhtml\">"+
						    quote.getQuotationLineItems().get(0).getTechnicalSpecs()+
						    "        </body>"+
						    "    </txtTechnicalSpecs>"+
						    "</subTechnicalDetails>"+
						    buildQuotationItemsSection(quote.getQuotationLineItems())+
						    "<subQuotationOptionalExtras>"+
						    buildOptionalExtraItems(quote.getOptionalExtraItems())+
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
	
	private String buildQuotationItemsSection(ArrayList<Stock> stockItems) {
		long subTotalAmount = 0;
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
		String subTotalAmountStr = new Long(subTotalAmount).toString();
		vatAmount = subTotalAmount * 0.14;
		String vatAmountStr = new Double(vatAmount).toString();
		String totalAmountStr = new Double(subTotalAmount + vatAmount).toString();
		
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
	
	private String buildOptionalExtraItems(ArrayList<OptionalExtra> optionalExtraItems) {
		String oeLineItems = "    <subHeading/>"+
						    "    <subTable>";
		for (OptionalExtra oe:optionalExtraItems) {
			oeLineItems +=	"        <subLineItem>"+
						    "            <txtLineItemDescription>"+oe.getDescription()+"</txtLineItemDescription>"+
						    "            <txtLineItemAmount>"+oe.getPricing()+"</txtLineItemAmount>"+
						    "        </subLineItem>";
		}
		oeLineItems += "    </subTable>";
		return oeLineItems;
	}
}
