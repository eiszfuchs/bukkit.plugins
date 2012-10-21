package de.eiszfuchs.bukkit.object;

import de.eiszfuchs.bukkit.nerdstuff.*;
import net.minecraft.server.*;
import org.bukkit.Material;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

/**
 * @author eiszfuchs
 */
public class Book {

    private String author;
    private String title;
    private String[] pages;

    public Book(ItemStack itemStack) throws FuckYouException {
        CraftItemStack bookItem = (CraftItemStack) itemStack;
        if (bookItem.getType() == Material.WRITTEN_BOOK) {
            NBTTagCompound bookData = bookItem.getHandle().tag;

            this.author = bookData.getString("author");
            this.title = bookData.getString("title");

            NBTTagList pagesData = bookData.getList("pages");

            String[] pagesString = new String[pagesData.size()];
            for (int i = 0; i < pagesData.size(); i++) {
                pagesString[i] = pagesData.get(i).toString();
            }

            this.pages = pagesString;
        } else {
            throw new FuckYouException();
        }
    }

    public Book(String title, String author, String[] pages) {
        this.title = title;
        this.author = author;
        this.pages = pages;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getPages() {
        return this.pages;
    }

    public ItemStack generateItemStack() {
        CraftItemStack newBook = new CraftItemStack(Material.WRITTEN_BOOK);

        NBTTagCompound newBookData = new NBTTagCompound();

        newBookData.setString("author", getAuthor());
        newBookData.setString("title", getTitle());

        NBTTagList pagesData = new NBTTagList();
        for (int i = 0; i < this.pages.length; i++) {
            pagesData.add(new NBTTagString(this.pages[i], this.pages[i]));
        }

        newBookData.set("pages", pagesData);

        newBook.getHandle().tag = newBookData;
        return (ItemStack) newBook;
    }
}
