SELECT
    s.location AS store_address,
    COUNT(sp.product_id) AS product_count
FROM
    stores s
        JOIN
    store_products sp ON s.store_id = sp.store_id
        JOIN
    products p ON sp.product_id = p.product_id
WHERE
        p.category_id = '?'
GROUP BY
    s.location
ORDER BY
    product_count DESC
    LIMIT 1;

--SELECT
   -- c.category_name,
    --s.location AS store_location,
    --SUM(d.product_count) AS total_products
--FROM
    --categories c
        --JOIN
    --products p ON c.category_id = p.category_id
        --JOIN
   -- deliveries d ON p.product_id = d.product_id
       -- JOIN
   -- stores s ON d.store_id = s.store_id
--WHERE
        --c.category_name = '?'
--GROUP BY
    --c.category_name, s.location
--ORDER BY
   -- total_products DESC
    --LIMIT 1;