package aronharder.itemizer;

/**
 * Keeps track of the Items in the Itemizer
 * Created by 2017-05-17
 * By Aron Harder
 */
public class Article {
    private String date;//The date associated with the article
    private String desc; //The article description, used for sorting articles
    private double amount; //The amount associated with the article

    /**
     * Initializes an empty article
     */
    public Article(){
        date = "01/01/2000";
        desc = "No description";
        amount = 0.0;
    }

    /**
     * Initializes an article with user input
     * @param init_date - the article date
     * @param init_desc - the article description
     * @param init_amount - the article amount
     */
    public Article(String init_date, String init_desc, double init_amount){
        date = init_date;
        desc = init_desc;
        amount = init_amount;
    }

    /**
     * Mutator to change article date
     * @param new_date - the new date
     */
    public void setDate(String new_date){
        date = new_date;
    }

    /**
     * Accessor to get the article date
     * @return date
     */
    public String getDate(){
        return date;
    }

    /**
     * Mutator to change article description
     * @param new_desc - the new description
     */
    public void setDesc(String new_desc) {
        desc = new_desc;
    }

    /**
     * Accessor to get the article description
     * @return desc
     */
    public String getDesc(){
        return desc;
    }

    /**
     * Mutator to change the article amount
     * @param new_amount - the new amount
     */
    public void setAmount(double new_amount){
        amount = new_amount;
    }

    /**
     * Accessor to get the article amount
     * @return amount
     */
    public double getAmount(){
        return amount;
    }

    /**
     * Checks if date > compare_date
     * @param compare_date - the date to compare against
     * @return whether the date of this article is after the compare date
     */
    public boolean date_greater_compare(String compare_date){
        String[] mdy = date.split("/");
        String[] compare_mdy = compare_date.split("/");
        if (Integer.valueOf(compare_mdy[2]) < Integer.valueOf(mdy[2])){ //compare_year < year
            return true;
        } else if (Integer.valueOf(compare_mdy[2]) > Integer.valueOf(mdy[2])){ //compare_year > year
            return false;
        }
        if (Integer.valueOf(compare_mdy[0]) < Integer.valueOf(mdy[0])){ //compare_month < month
            return true;
        } else if (Integer.valueOf(compare_mdy[0]) > Integer.valueOf(mdy[0])){ //compare_month > month
            return false;
        }
        if (Integer.valueOf(compare_mdy[1]) < Integer.valueOf(mdy[1])){ //compare_day < day
            return true;
        } else if (Integer.valueOf(compare_mdy[1]) > Integer.valueOf(mdy[1])){ //compare_day > day
            return false;
        }
        return false; //Same date
    }

    /**
     * Checks if date < compare_date
     * @param compare_date - the date to compare against
     * @return whether the date of this article is before the compare date
     */
    public boolean date_less_compare(String compare_date){
        String[] mdy = date.split("/");
        String[] compare_mdy = compare_date.split("/");
        if (Integer.valueOf(compare_mdy[2]) > Integer.valueOf(mdy[2])){ //compare_year > year
            return true;
        } else if (Integer.valueOf(compare_mdy[2]) < Integer.valueOf(mdy[2])){ // compare_year < year
            return false;
        }
        if (Integer.valueOf(compare_mdy[0]) > Integer.valueOf(mdy[0])){ //compare_month > month
            return true;
        } else if (Integer.valueOf(compare_mdy[0]) < Integer.valueOf(mdy[0])){ //compare_month < month
            return false;
        }
        if (Integer.valueOf(compare_mdy[1]) > Integer.valueOf(mdy[1])){ //compare_day > day
            return true;
        } else if (Integer.valueOf(compare_mdy[1]) < Integer.valueOf(mdy[1])){ //compare_day < day
            return false;
        }
        return false; //Same date
    }

    /**
     * Checks if date == compare_date
     * @param compare_date - the date to compare against
     * @return whether the date of this article is the same day as the compare date
     */
    public boolean date_equals_compare(String compare_date){
        return date.equals(compare_date);
    }
}
