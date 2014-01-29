create table las_stock.user (
    user_id INT NOT NULL AUTO_INCREMENT,
    user_name VARCHAR(255),
    user_password VARCHAR(255),
    user_display_name VARCHAR(255),
    user_email_address VARCHAR(255),
    user_contact_number VARCHAR(63),
    user_admin INT,
    user_enabled INT,
    PRIMARY KEY (user_id)
);

create table las_stock.stock (
    stock_id INT NOT NULL AUTO_INCREMENT,
    stock_code VARCHAR(45),
    stock_description VARCHAR(255),
    stock_manufacturer VARCHAR(255),
    stock_model VARCHAR(255),
    stock_series VARCHAR(255),
    pricing DOUBLE,
    technical_specs VARCHAR(1023),
    stock_used INT,
    stock_markup INT,
    stock_shipping INT,
    PRIMARY KEY (stock_id)
);

create table las_stock.stock_install (
    stock_id INT,
    install_location VARCHAR(255),
    install_price DOUBLE
);

create table las_stock.stock_level(
    stock_id INT,
    serial_number VARCHAR(45),
    stock_status INT
);

create table las_stock.accessory (
    accessory_id INT NOT NULL AUTO_INCREMENT,
    accessory_code VARCHAR(45),
    accessory_description VARCHAR(255),
    accessory_model VARCHAR(255),
    accessory_manufacturer VARCHAR(255),
    pricing DOUBLE,
    accessory_markup INT,
    accessory_shipping INT,
    PRIMARY KEY (accessory_id)
);

create table las_stock.accessory_level(
    accessory_id INT,
    serial_number VARCHAR(45),
    accessory_status INT
);

create table las_stock.quotation (
    quotation_id INT NOT NULL AUTO_INCREMENT,
    quotation_date VARCHAR(255),
    customer_id INT,
    user_id INT,
    status VARCHAR(56),
    rate VARCHAR(56),
    notes VARCHAR(2047),
    delivery VARCHAR(2047),
    installation VARCHAR(2047),
    warranty VARCHAR(2047),
    variation VARCHAR(2047),
    validity VARCHAR(2047),
    installation_location VARCHAR(255),
    installation_price DOUBLE,
    used_item INT,
    company_id INT,
    show_item_prices VARCHAR(5),
    PRIMARY KEY (quotation_id)
);

create table las_stock.quotation_line_item (
    stock_id INT,
    serial_number VARCHAR(45),
    quotation_id INT,
    accessory_id INT,
    pricing DOUBLE
);

/*create table las_stock.optional_extra (
    optional_extra_id INT NOT NULL AUTO_INCREMENT,
    optional_extra_description VARCHAR(255),
    pricing DOUBLE,
    PRIMARY KEY (optional_extra_id)
);*/
    
create table las_stock.customer (
    customer_id INT NOT NULL AUTO_INCREMENT, 
    customer_name VARCHAR(255),
    customer_address VARCHAR(255),
    customer_email_address VARCHAR(255),
    customer_phone_number VARCHAR(45),
    customer_attention VARCHAR(255),
    PRIMARY KEY (customer_id)
);

create table las_stock.defaults (
    notes VARCHAR(2047),
    delivery VARCHAR(2047),
    installation VARCHAR(2047),
    warranty VARCHAR(2047),
    used_warranty VARCHAR(2047),
    variation VARCHAR(2047),
    validity VARCHAR(2047)
);

create table las_stock.stock_image (
    stock_id INT,
    stock_image LONGBLOB
);

insert into 
    las_stock.defaults (notes, delivery, installation, warranty, variation, validity, used_warranty)
values (
    "<ul><li>No Allowance has been made for: PTO, Sub frame, body modifications, moving of batteries, exhaust, brackets etc.</li></ul>",
    "<ul><li>Ex stock, subject to prior sale.</li></ul>",
    "<ul><li>Approximately 5-7 working days, however it's subject to factory workload at time of order, as well as the availability of labour and material.</li></ul>",
    "<ul><li>New equipment carries a 36 months warranty on all structural components.</li><li>Consumable parts such as seals, hoses etc, as well as general wear and tear of the platform carries a 12 months warranty.</li></ul>",
    "<ul><li>All prices quoted are not firm and are based on an exchange rate of Euro 1.00 = ZAR <<rate>></li><li>Changes to this rate as at date of dispatch ex our premises, together with the effect of such changes on landing/clearing charges, including surcharges and the like, will be for the customer's account.</li><li>Forward cover can be established at any time, upon customer's specific written request and adjusted price will be quantified, once this has been established.</li></ul>",
    "<ul><li>Quotation will remain valid for a period of FOURTEEN (14) days and the Platform Price is subject to the Price Variation Clause.</li></ul>",
    "<ul><li>Used equipment does not carry any warranty.</li></ul>" 
);

    