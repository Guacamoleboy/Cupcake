-- Galleri opslag med eksisterende billeder
INSERT INTO gallery_posts (image_url, description, user_id, size_class) VALUES
('/images/gallery/birthday.png', 'Fødselsdags Cupcake - denne gav vi til Jonas og hans boyfriend på deres fødselsdag!', 1, 'gallery-item--extreme'),
('/images/gallery/pride.png', 'Fejring af Jødisk Kulturfestival - Andreas stod for at lave regnbuecupcakes til fejringen!', 1, 'gallery-item--large'),
('/images/gallery/nice.png', 'Lækker jordbær LOL', 1, 'gallery-item--medium'),
('/images/gallery/andreas.png', 'Andreas Special', 1, 'gallery-item--large'),
('/images/gallery/jew.png', 'Blåbær muffins med et twist af LGTBQ+ krymmel! We don''t discriminate.', 1, 'gallery-item--medium'),
('/images/gallery/ebou.png', 'Olskers Special Cookie Monster cupcakes, som vores elskede Jonas fik spist et par af!', 1, 'gallery-item--large');

-- Sample likes fra brugere
INSERT INTO gallery_likes (post_id, user_id) VALUES
(1, 1),
(1, 2),
(2, 1),
(2, 3),
(3, 2),
(4, 1),
(5, 3);

-- Sample kommentarer på opslag
INSERT INTO gallery_comments (post_id, user_id, comment_text) VALUES
(1, 1, 'add'),
(1, 1, 'føj'),
(2, 1, 'wtf!'),
(3, 1, 'ulækkert?'),
(4, 1, 'Andreas laver altid de klammeste cupcakes');
