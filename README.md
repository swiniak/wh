## Disclaimer:
* That's my first time with Selenium in Java as well as Cucumber... or frankly speaking BDD in general.
* So far I've only read about it few times but I did not like the idea.
* Finding few hours to get through this was challenging (lots of responsibilities at work + 2 kids + bathroom makeover
  in progress).
* I know nothing about football which made navigating to Premiership football event a real puzzle.
* I know nothing about gambling (mostly because the few times that I've tried I've lost).
* I guess I'm not good at doing simple one-time use tasks. I'm trying to anticipate how this may evolve and prepare for\
the future maitenance and development which that takes unnecessary time in this particular case. I've probably
overcomplicated this 
* Also my Java is very rusty but you can see that for yourself :)

## Issues, questions, concerns
* I find it strange that this site redirects to HTTP even when I try to use HTTPS
* These are kind of worrying:
  * `<a title="Shop Locator" href="http://31.193.137.143/whill/locator2/public/" target="_blank">Shop Locator</a>`
  * `SWFObject v2.1 <http://code.google.com/p/swfobject/> Copyright (c) 2007-2008`
  * `PSConFlash.js 1487 2009-12-04 16:55:59Z kelby Copyright (c) 2009`
* I hope you are aware that your certificate expires in March
* It takes forever to execute this simple test on my side because of sports.staticcache.org\
  So long that I would like to throw timeout exception myself - it took aprox 40s (sic!) with each pageload via my ISP
* Fun fact: I can login on WHITA_opex1-10 with same password (btw - it's 2018 now so might be worth changing it ;) )\
  I understand these are some fake testing accounts, still security-wise 
* Could I get extra points if I was able to withdraw these testing funds? I have not tried but it seems tempting.\
  Probably it's not that easy so even more tempting.
* Using a logger in these tests would be a good idea
* Based on my past experiences I prefer not to use page factory @FindBy annotations

## And for something completely different now
I've enjoyed this little exercise. Thank you.