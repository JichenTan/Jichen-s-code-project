const puppeteer = require('puppeteer');

(async () => {

    const extractProducts = async url => {

    
    const page = await browser.newPage(); //open a new page
    await page.goto(url);
    await page.waitFor(1000);

// convert a node list to an array
    const productsOnPage = await page.evaluate(() => 
         Array.from(document.querySelectorAll('div.col-sm-4.col-lg-4.col-md-4'))
            .map(product => ({
                name : product.querySelector('a.title').innerText,
                price : product.querySelector('h4.pull-right.price').innerText,
                //image ：product.querySelector('img.img-responsive').src
            }))
    ); 

    await page.close();

    if (productsOnPage.length < 1) {
      // Terminate if no partners exist
      return productsOnPage
    } else {
      // Go fetch the next page ?page=X+1
      const nextPageNumber = parseInt(url.match(/page=(\d+)$/)[1], 10) + 1;
      const nextUrl = `https://www.webscraper.io/test-sites/e-commerce/static/computers/laptops?page=${nextPageNumber}`;

      return productsOnPage.concat(await extractProducts(nextUrl))
    }
  };


    const browser = await puppeteer.launch({headless: false});        
    const firstUrl = 'https://www.webscraper.io/test-sites/e-commerce/static/computers/laptops?page=1';
    const products =  await extractProducts(firstUrl);
    console.log(products);

    await browser.close();

    
})();