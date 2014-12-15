Source Files and What They Do
-----------------------------------
HomeActivity - Home screen activity, lets you search, select city/category, and just browse

CityPicker - Top level region selector, gets all the craigslist sites and puts them in a listview
CpLevelTwo - Second level region selector, gets city list based on site chosen

CatPicker - Top level category selector, gets all the craigslist categories and puts them in a listview
CatLevelTwo - Second level category selector, gets sub categories based on main category 

ViewActivity - when Go or Search button is pressed this activity starts. It takes the category URL and populates the post list.
ViewPagerAdapter - handles the switching of the thumb and list tabs.
ThumbListAdapter - custom listview adapter

ListFragment - Takes title, city, date, price, map, and pic and puts them in each listview entry.
ThumbFragment - not used

PostActivity - when a post is selected in the list this is called. It's passed that post's URL.
PostPagerAdapter - handles tabbing between Post and Map
PostFragment - Formats the post data.
PostMapFragment - shows post location if there is one.
Post - data type that holds all the post info.


3rd Party Libraries
------------------------------------
jsoup - Java HTML Parser
http://jsoup.org/

pull to refresh
https://github.com/naver/android-pull-to-refresh


DEMO LINK
------------------------------------
https://www.youtube.com/watch?v=NGTXSL1k_Qo