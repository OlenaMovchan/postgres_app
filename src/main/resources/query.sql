SELECT
    c.category_name,
    s.location AS store_location,
    SUM(d.product_count) AS total_products
FROM
    categories c
        JOIN
    products p ON c.category_id = p.category_id
        JOIN
    deliveries d ON p.product_id = d.product_id
        JOIN
    stores s ON d.store_id = s.store_id
WHERE
        c.category_name=?
GROUP BY
    c.category_name, s.location
ORDER BY
    total_products DESC
    LIMIT 1;