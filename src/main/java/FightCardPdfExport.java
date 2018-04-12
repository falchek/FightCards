import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


import com.lowagie.text.*;

//import com.lowagie.text.List;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;


//Writes a fight card to pdf.
public class FightCardPdfExport {

    public static final String FILENAME = "src/main/resources/FightCards.pdf";
    private List<FightCard> fightCards;


    public FightCardPdfExport(List<FightCard> fightCards){
        this.fightCards = fightCards;
    }


    public void writeFightCards() throws DocumentException, IOException {

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(FILENAME));

        document.open();

        for(FightCard card : fightCards) {
            document.add(getNameHeader(card.getOwner().getName()));


            document.add(createInstructionsHeader());
            document.add(createListOfInstructions());
            document.add(createWinInstructionsLine());
            document.add(Chunk.NEWLINE);
            document.add(createOpponentsTable(card));
            document.newPage();
        }
        document.close();

    }

    public Paragraph getNameHeader(String fighterName) {
        Paragraph name = new Paragraph();
        name.setAlignment(Paragraph.ALIGN_CENTER);

        Font f = new Font(Font.HELVETICA, 18.0f, Font.BOLD);
        name.setFont(f);
        name.add(fighterName);

        return name;
    }

    public PdfPTable createOpponentsTable(FightCard card) {

        //get longest name.
        //get longest park.
        PdfPTable table = new PdfPTable(3);
        float[] widths = {1,1,3};
        try {
            table.setWidths(widths);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        table.addCell("Name:");
        table.addCell("Park");
        table.addCell("Signature"); //this is the line for signature.
        table.setWidthPercentage(100.0F);
        for(Fighter opponent : card.getOpponents()){
            table.addCell(opponent.getName());
            table.addCell(opponent.getLocation());
            table.addCell(""); //this is the line for signature.
        }

        return table;
    }

    //returns a bulleted list.
    public Paragraph createListOfInstructions() {
        Paragraph listWrapper = new Paragraph();
        com.lowagie.text.List list = new com.lowagie.text.List();
        list.setSymbolIndent(12);
        list.setListSymbol("\u2022");
        list.add("  Find each person on your list, and fight them 10 times.  Don’t worry about score!");
        list.add("  Sign each other’s cards.  Be sure to help your partner find anyone they have yet to fight. ");
        list.add("  Turn this card to Tato or Troll by 6pm Saturday");
        listWrapper.add(list);
        listWrapper.setIndentationLeft(12.0f);

        return listWrapper;
    }

    public Paragraph createInstructionsHeader() { //just say instructions, first.
        Paragraph instructions = new Paragraph();
        instructions.add("Instructions:");
        return instructions;
    }

    public Paragraph createWinInstructionsLine(){
        Paragraph instructions = new Paragraph();
        Font f = new Font(Font.HELVETICA, 12.0F, Font.ITALIC);
        instructions.setFont(f);
        instructions.add("For each signature, you will get one ticket in the WLS card raffle up to 10 tickets.");
        return instructions;
    }



}
