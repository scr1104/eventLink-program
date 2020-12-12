package Gateway;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class TicketPrint_pdf_ver {


    /**
     * Generates a pdf file with ticket information.
     *
     * @param userID currentUser ID
     * @param userName currentUser name
     * @param eventName name of the chosen event
     * @param time time of the chosen event
     * @param userType type of the currentUser
     * @param roomNo number of the room of the chosen event
     * @throws IOException
     */
        public void generateTicket(String userID, String userName, String eventName,
                                   String time, String userType, int roomNo) throws IOException {
                //Loading an existing document
                String roomName = Integer.toString(roomNo);
                PDDocument doc = PDDocument.load(new File("phase2/ticket_base.pdf"));

                //Creating a PDF Document
                PDPage page = doc.getPage(0);
                PDFont pdfFont= PDType1Font.HELVETICA_OBLIQUE;
                PDPageContentStream cos = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true);

                //Name of event
                cos.beginText();
                cos.setFont(PDType1Font.HELVETICA_BOLD, 30);
                cos.newLineAtOffset(82, 660 );
                cos.showText(eventName);
                cos.endText();

                //Usertype
                cos.beginText();
                cos.setFont(pdfFont, 13);
                cos.newLineAtOffset(158, 623);
                cos.showText(userType);
                cos.endText();

                //Name
                cos.beginText();
                cos.setFont(pdfFont, 13);
                cos.newLineAtOffset(131, 578 );
                cos.showText(userName);
                cos.endText();

                //UserId
                cos.beginText();
                cos.setFont(pdfFont, 13);
                cos.newLineAtOffset(157, 550 );
                cos.showText(userID);
                cos.endText();

                cos.beginText();
                cos.setFont(pdfFont, 13);
                cos.newLineAtOffset(128, 520 );
                cos.showText(time);
                cos.endText();

                cos.beginText();
                cos.setFont(pdfFont, 13);
                cos.newLineAtOffset(131, 492 );
                cos.showText(roomName);
                cos.endText();
                cos.close();

                try {
                        doc.save(new File("phase2/ticket_"+userID+"_"+eventName+".pdf"));
                } catch (IOException ioe) {
                        ioe.printStackTrace();
                }

                doc.close();

        }




    }
