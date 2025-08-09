
CREATE TABLE payment_types (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               name TEXT NOT NULL,
                               duration_from TIMESTAMP NOT NULL,
                               duration_to TIMESTAMP,
                               added_by BIGINT NOT NULL,
                               deleted_by BIGINT
);

CREATE TABLE scheduled_notifications (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         id_notification_type BIGINT NOT NULL,
                         scheduled_time TIMESTAMP,
                         id_reservation BIGINT NOT NULL
);

CREATE TABLE status (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        name TEXT NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        added_by BIGINT NOT NULL
);


CREATE TABLE payment (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         payment_type_id BIGINT NOT NULL,
                         status TEXT NOT NULL,
                         date_paid TIMESTAMP,
                         amount DECIMAL(10, 2) NOT NULL,
                         FOREIGN KEY (payment_type_id) REFERENCES payment_types(id)
);


CREATE TABLE reservation (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             date TIMESTAMP NOT NULL,
                             id_company BIGINT NOT NULL,
                             id_service BIGINT NOT NULL,
                             id_customer INT NOT NULL,
                             send_sms TIMESTAMP,
                             notified BOOLEAN,
                             `2FA_code` BIGINT,
                             hidden BOOLEAN DEFAULT FALSE,
                             customer_email VARCHAR(255),
                             customer_phone_number BIGINT,
                             customer_full_name VARCHAR(255),
                             status_id BIGINT,
                             payment_id BIGINT,
                             id_employee bigint,
                             FOREIGN KEY (status_id) REFERENCES status(id),
                             FOREIGN KEY (payment_id) REFERENCES payment(id)
);

-- Table: company
CREATE TABLE company (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         description TEXT,
                         id_sms_notification BIGINT,
                         id_picture TEXT,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         id_auth_user VARCHAR(255),
                         uuid_url VARCHAR(255),
                         address VARCHAR(255),
                         phone_number VARCHAR(20),
                         email VARCHAR(255),
                         first_name VARCHAR(255),
                         last_name VARCHAR(255),
                         company_name VARCHAR(255),
                         id_location_counrty BIGINT
);

-- Table: location
CREATE TABLE location (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          street VARCHAR(255),
                          number VARCHAR(255),
                          id_location BIGINT
);

-- Table: services
CREATE TABLE services (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          id_category BIGINT,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          id_company BIGINT,
                          name VARCHAR(255),
                          description TEXT,
                          price FLOAT,
                          id_picture TEXT,
                          duration SMALLINT,
						  id_location BIGINT
);

-- Table: service_categories
CREATE TABLE service_categories (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    name VARCHAR(255),
                                    id_company BIGINT,
									id_location BIGINT
);

-- Table: business_hours
CREATE TABLE business_hours (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                day_number INT,
                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                time_from TIME,
                                time_to TIME,
                                pause_to TIME,
                                pause_from TIME,
                                id_company BIGINT,
                                id_location BIGINT,
                                day VARCHAR(50)
);

-- Table: sms_notification_config
CREATE TABLE sms_notification_config (
                                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                         name VARCHAR(255)
);

            