const words = document.querySelectorAll('.word');

words.forEach(word => {
  word.addEventListener('click', () => {
    const tooltip = word.querySelector('.tooltip');
    tooltip.style.display = 'block';
  });
});

