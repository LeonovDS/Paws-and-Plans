const words = document.querySelectorAll('.word');

words.forEach(word => {
  word.addEventListener('click', () => {
    const tooltip = word.querySelector('.tooltip');
    tooltip.style.display = 'block';
  });
});

// Получаем элемент с классом "circle1"
const circle1 = document.querySelector('.circle1');

// Добавляем обработчик события "click"
circle1.addEventListener('click', () => {
    // Открываем страницу "second_page.html"
    window.location.href = 'second_page.html';
});

