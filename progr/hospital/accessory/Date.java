package accessory;

public class Date{
    int day;
    int month;
    int year;

    public Date(int day, int month, int year)
    {
        if (day <= 0 || month <= 0 || month > 12)
        {
            System.out.println("CeasarError, Date out of Range");
        }
        else if(day > 31 && (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12))
        {
            System.out.println("CeasarError, Date out of Range");
        }
        else if(day > 30 && (month == 4 || month == 6 || month == 9 || month == 11))
        {
            System.out.println("CeasarError, Date out of Range");
        }
        else if(day > 29 && month == 2 && year % 4 == 0)
        {
            System.out.println("CeasarError, Date out of Range");
        }
        else if(day > 28 && month == 2 && year % 4 != 0)
        {
            System.out.println("CeasarError, Date out of Range");
        }
        else
        {
            this.day = day;
            this.month = month;
            this.year = year;
        }
    }

    public void display()
    {
        System.out.println("----------------------");
        System.out.println(this.day + "-" + this.month + "-" + this.year);
        System.out.println("----------------------");
    }
     
    public int compare(Date date)
    {
        //0 = Pareil
        //1 = this est sup
        //-1 this est inf
        if(this.year > date.year)
        {
            return 1;
        }
        else if(this.year < date.year)
        {
            return -1;
        }
        else
        {
            if(this.month > date.month)
            {
                return 1;
            }
            else if(this.month < date.month)
            {
                return -1;
            }
            else
            {
                if(this.day > date.day)
                {
                    return 1;
                }
                else if(this.day < date.day)
                {
                    return -1;
                }
                else
                {
                    return 0;
                }
            }
        }
    }

    public boolean bissextile()
    {
        int cond1 = this.year % 4;
        int cond2 = this.year % 100;
        int cond3 = this.year % 400;
        if(cond1 == 0 || cond2 == 0 || cond3 == 0)
        {
            return true;
        }
        return false;
    }

    public void add_day(int add_day)
    {
        int[] months = new int[12];
        months[0] = 31;
        if(this.bissextile() == false)
        {
            months[1] = 28;
        }
        else
        {
            months[1] = 29;
        }
        months[2] = 31;
        months[3] = 30;
        months[4] = 31;
        months[5] = 30;
        months[6] = 31;
        months[7] = 31;
        months[8] = 30;
        months[9] = 31;
        months[10] = 30;
        months[11] = 31;

        this.day += add_day;
        

        if(add_day > 0)
        {
            while(this.day > months[this.month - 1])
            {
                this.day -= months[this.month - 1];
                this.month++;
                if(this.month > 12)
                {
                    this.year += this.month / 12;
                    this.month %= 12;
                }
            }
        }
        if(add_day < 0)
        {
            while(this.day <= 0)
            {
                this.month--;
                if(this.month <= 0)
                {
                    this.year--;
                    this.month = 12;
                }
                this.day += months[this.month - 1];

            }
        }
    }

    public int age()
    {
        Date today = new Date(27, 03, 2026);
        int age = today.year - this.year;
        if(today.month > this.month)
        {
            if(today.day > this.day)
            {
                age--;
            }
        }
        return age;
    }

    public String toString()
    {
        return this.day + "/" + this.month + "/" + this.year;
    }
}
