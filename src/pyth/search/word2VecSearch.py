from gensim.models import Word2Vec
from nltk.tokenize import word_tokenize
from nltk.corpus import stopwords
from nltk.stem import WordNetLemmatizer
import numpy as np
from sklearn.metrics.pairwise import cosine_similarity
from collections import defaultdict

# Не работает

# Загрузка предварительно обученной модели Word2Vec

word2vec_model = Word2Vec.load("s3://auxdata.johnsnowlabs.com/public/models/w2v_cc_300d_ru_3.4.1_3.0_1647455083959.zip")

# Функция для предобработки текста: токенизация, удаление стоп-слов и лемматизация
def preprocess_text(text):
    tokens = word_tokenize(text.lower())
    stop_words = set(stopwords.words('russian'))
    tokens = [word for word in tokens if word.isalnum() and word not in stop_words]
    lemmatizer = WordNetLemmatizer()
    tokens = [lemmatizer.lemmatize(word) for word in tokens]
    return tokens

# Функция для создания векторного представления текста с использованием модели Word2Vec
def text_to_vector(text):
    tokens = preprocess_text(text)
    vectors = []
    for token in tokens:
        if token in word2vec_model.wv:
            vectors.append(word2vec_model.wv[token])
    if vectors:
        return np.mean(vectors, axis=0)
    else:
        return np.zeros(word2vec_model.vector_size)

# Функция для вычисления косинусной схожести между запросом и текстом книги
def calculate_similarity(query_vector, book_vector):
    similarity_score = cosine_similarity([query_vector], [book_vector])[0][0]
    return similarity_score

# Функция для выполнения интеллектуального поиска по всем книгам
def searching(query, books):
    relevance_map = defaultdict(float)
    # Получение векторного представления запроса
    query_vector = text_to_vector(query)
    for book in books:
        # Сбор информации о книге
        book_text = ' '.join([book['title'], book['description'],
                              ' '.join(book['authors']), ' '.join(book['genres']),
                              book['publisher'], str(book['price'])])
        # Получение векторного представления текста книги
        book_vector = text_to_vector(book_text)
        # Расчет схожести между запросом и книгой
        relevance = calculate_similarity(query_vector, book_vector)
        relevance_map[book['id']] = relevance
    return relevance_map