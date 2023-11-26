
SELECT
    s.location AS store_location,
    SUM(sp.quantity) AS total_products
FROM
    store_products sp
        JOIN
    (
        SELECT
            p.product_id,
            p.category_id
        FROM
            products p
                JOIN
            categories c ON p.category_id = c.category_id
        WHERE
                c.category_name = 'Спорт'
    ) AS filtered_products ON sp.product_id = filtered_products.product_id
        JOIN
    stores s ON sp.store_id = s.store_id
GROUP BY
    s.location
ORDER BY
    total_products DESC
    LIMIT 1;