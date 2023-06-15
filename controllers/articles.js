const services = require('../helper/service');
const cheerio = require('cheerio');

const baseUrl = 'https://www.detik.com/';

async function getData(query, res) {
    const response = await services.fetchService(`${baseUrl}search/searchall?query=${query}`, res);
    const $ = cheerio.load(response.data);

    const rows = $('article');
    const articles = [];

    const urlSelector = 'a';
    const thumbnailSelector = 'img';
    const titleSelector = 'img';
    const dateSelector = '.date';

    rows.each((index, element) => {
        const row = $(element);

        // Mengambil elemen di dalam row
        const url = row.find(urlSelector).attr('href');
        const thumbnail = row.find(thumbnailSelector).attr('src');
        const title = row.find(titleSelector).attr('alt');
        const date = row.find(dateSelector).contents().last().text().trim();

        const data = {
            title,
            photoUrl: thumbnail,
            linkUrl: url,
            date
        };

        articles.push(data);
    });

    return articles;
}

const getFishArticles = async (req, res) => {
    try {
        const query1 = "resep+ikan+laut";
        const query2 = "ikan+laut+solusi+stunting";
        const query3 = "tips+ikan+laut+segar";

        const [articles1, articles2, articles3] = await Promise.all([
            getData(query1, res),
            getData(query2, res),
            getData(query3, res)
        ]);

        const articles = [...articles1, ...articles2, ...articles3].sort(() => Math.random() - 0.5);;
        const uniqueArticles = [...new Set(articles)];

        return res.status(200).json({
            status: true,
            articles: uniqueArticles
        });
    } catch (error) {
        return res.status(500).json({ message: 'Error get articles' });
    }
};

module.exports = {
    getFishArticles
};