const puppeteer = require('puppeteer');

(async () => {
    const browser = await puppeteer.launch({headless: false});
    const page = await browser.newPage();

    await page.goto('https://www.webscraper.io/test-sites/e-commerce/static/computers/laptops');
    
    await page.waitFor(1000);


// convert a node list to an array
    const names = await page.evaluate(() => 
    Array.from(document.querySelectorAll('div.col-sm-4.col-lg-4.col-md-4 a.title')).map((name)=>name.innerText)
        )

    const prices = await page.evaluate(() => 
    Array.from(document.querySelectorAll('div.col-sm-4.col-lg-4.col-md-4 h4.pull-right.price')).map((price)=>price.innerText)
        )

    const images = await page.evaluate(() => 
    Array.from(document.querySelector('div.col-sm-4.col-lg-4.col-md-4 img.img-responsive')).map((image)=>image.src)
        )

    const products = await page.evaluate(() => 
        Array.from(document.querySelectorAll('div.col-sm-4.col-lg-4.col-md-4'))
            .map(product => ({
                name : product.querySelector('a.title').innerText,
                price : product.querySelector('h4.pull-right.price').innerText,
                //image ：product.querySelector('img.img-responsive').src

                
            }))
    )

  
    //console.log(names);
    //console.log(prices);
    //console.log(images);
    console.log(products);
    await browser.close();
    
})();