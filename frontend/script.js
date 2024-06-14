const mainLink = document.querySelector('.shop-button a');
const overlay = document.getElementById('overlay');
const popup = document.querySelector('.pop-up');

mainLink.addEventListener('click', () => {
  overlay.style.display = 'block';
  popup.style.display = 'block';
});

overlay.addEventListener('click', () => {
  overlay.style.display = 'none';
  popup.style.display = 'none';
});


// Получаем элемент с классом "circle1"
const circle1 = document.querySelector('.circle1');

// Добавляем обработчик события "click"
circle1.addEventListener('click', () => {
    // Открываем страницу "second_page.html"
    window.location.href = 'second_page.html';
});

