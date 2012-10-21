package de.eiszfuchs.bukkit.command;

import de.eiszfuchs.bukkit.BookPublisher;
import de.eiszfuchs.bukkit.nerdstuff.*;
import de.eiszfuchs.bukkit.object.*;
import java.io.*;
import java.util.Date;
import java.util.Locale;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author eiszfuchs
 */
public class BookPublisherCommand implements CommandExecutor {

    private BookPublisher mother;
    
    public BookPublisherCommand(BookPublisher mother) {
        this.mother = mother;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        if (sender instanceof Player) {
            try {
                Book book = new Book(((Player) sender).getItemInHand());
                
                String path = this.mother.getConfig().getString("publish_path");
                File bookFile = new File(path);
                
                if (bookFile.isDirectory()) {
                    Date now = new Date();
                    String bookName = sender.getName().toLowerCase() + "-" + String.valueOf(now.getTime());
                    
                    bookFile = new File(path + "/" + bookName + ".md");
                    try {
                        bookFile.createNewFile();
                        BufferedWriter buffer = new BufferedWriter(
                            new OutputStreamWriter(
                                new FileOutputStream(bookFile),
                            "UTF-8")
                        );
                        
                        String content = "";
                        content += "Title: " + book.getTitle() + "\n";
                        content += "Author: " + book.getAuthor() + "\n";
                        content += "Published: " + String.valueOf(now.getTime()) + "\n";
                        content += "\n";
                        
                        int pageCount = book.getPages().length;
                        String[] pages = book.getPages();
                        
                        for (int i = 0; i < pageCount; i += 1) {
                            content += pages[i].toString();
                        }
                        
                        buffer.write(content);
                        buffer.close();
                        
                        sender.sendMessage("Datei angelegt: " + bookFile.getName());
                    } catch (IOException exception) {
                        sender.sendMessage("Konnte Datei nicht anlegen.");
                    }
                } else {
                    sender.sendMessage("Pfad in der Konfiguration nicht vorhanden / lesbar.");
                }
                
                return true;
            } catch (FuckYouException exception) {
                sender.sendMessage("Das ist kein abgeschlossenes Buch in Deiner Hand!");
                return false;
            }
        }
        return false;
    }
    
}
