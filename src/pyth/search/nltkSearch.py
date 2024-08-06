from nltk.tokenize import word_tokenize
from nltk.corpus import stopwords
from nltk.stem import WordNetLemmatizer
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity
import nltk

#обычный поиск по четким совпадениям
def preprocess_text(text):
    # Приведение к нижнему регистру
    text = text.lower()
    # Токенизация
    tokens = word_tokenize(text)
    # Удаление стоп-слов
    stop_words = set(stopwords.words('russian'))
    tokens = [word for word in tokens if word not in stop_words]
    # Лемматизация
    lemmatizer = WordNetLemmatizer()
    tokens = [lemmatizer.lemmatize(word) for word in tokens]
    # Склейка токенов обратно в строку
    processed_text = ' '.join(tokens)
    return processed_text

def searching(search_query, books):
    nltk.download('punkt')
    nltk.download('stopwords')
    nltk.download('wordnet')

    # Предобработка запроса
    processed_query = preprocess_text(search_query)
    # Предобработка описаний книг
    processed_descriptions = [preprocess_text(book['description']) for book in books]
    # Вычисление TF-IDF
    vectorizer = TfidfVectorizer()
    tfidf_matrix = vectorizer.fit_transform([processed_query] + processed_descriptions)
    # Вычисление косинусного сходства между запросом и описаниями книг
    similarities = cosine_similarity(tfidf_matrix[0:1], tfidf_matrix)[0][1:]
    # Создание словаря id -> relevance
    relevance_map = {book['id']: similarity for book, similarity in zip(books, similarities)}
    # Фильтрация по порогу релевантности
    filtered_relevance_map = {book_id: relevance for book_id, relevance in relevance_map.items() if relevance >= 0}
    return filtered_relevance_map

# вторая реализация (не проверял)
import nltk
from nltk.tokenize import word_tokenize
from nltk.corpus import stopwords
from nltk.stem import PorterStemmer
from collections import defaultdict

# Инициализация NLTK
nltk.download('punkt')
nltk.download('stopwords')

# Создание объекта PorterStemmer для стемминга слов
ps = PorterStemmer()

# Функция для предобработки текста: токенизация, удаление стоп-слов и стемминг
def preprocess_text(text):
    tokens = word_tokenize(text.lower())
    stop_words = set(stopwords.words('russian'))
    tokens = [ps.stem(w) for w in tokens if not w in stop_words and w.isalnum()]
    return tokens

# Функция для расчета релевантности книги к запросу
def calculate_relevance(book, query):
    relevance = 0
    # Предобработка запроса
    query_tokens = preprocess_text(query)
    # Предобработка текста книги
    book_tokens = preprocess_text(book['title'] + ' ' + book['description'] + ' '.join(book['authors']) + ' '.join(book['genres']))
    # Подсчет совпадающих токенов
    common_tokens = set(query_tokens) & set(book_tokens)
    relevance = len(common_tokens) / max(len(query_tokens), 1) # избегаем деления на ноль
    return relevance

# Функция для выполнения поиска по всем книгам
def intelligent_search(books, query):
    relevance_map = defaultdict(float)
    for book in books:
        relevance = calculate_relevance(book, query)
        relevance_map[book['id']] = relevance
    return relevance_map