package com.example.nako.thetreediary.myDatabase;

import java.util.ArrayList;

/**
 * Created by Nako on 2017/1/10.
 */

public class TreeClass {
    private String TREE = "DailyLife";
    private String DESCRIPTION = "Description...";
    private int ARTICLE_NUM = 0;
    private int TREE_PHOTO = 0;

    public void SetTree(String TREE) {this.TREE = TREE;}
    public void SetDescription(String DESCRIPTION) {this.DESCRIPTION = DESCRIPTION;}
    public void SetArticleNumber(int ARTICLE_NUM) {this.ARTICLE_NUM = ARTICLE_NUM;}
    public void SetTreePhoto(int TREE_PHOTO) {this.TREE_PHOTO = TREE_PHOTO;}

    public String GetTree() {return this.TREE;}
    public String GetDescription() {return this.DESCRIPTION;}
    public int GetArticleNumber() {return  this.ARTICLE_NUM;}
    public int GetTreePhoto() {return this.TREE_PHOTO;}

    public ArrayList<String> TreeInfo(){
        ArrayList<String> info = new ArrayList<String>();
        info.add(this.TREE);
        info.add(this.DESCRIPTION);
        info.add(String.valueOf(this.ARTICLE_NUM));
        info.add(String.valueOf(this.TREE_PHOTO));
        return info;
    }
}
