document.addEventListener('DOMContentLoaded', () => {
    loadHistory();
});

async function loadHistory() 
{
    try 
    {
        const response = await fetch('/fcgi-bin/lab2-server.jar?request=CREATE_TABLE', {
            method: 'GET',
            headers: {
                'Accept': 'text/html'
            }
        });

        const respText = await response.text();
        if( !response.ok ) 
        {
            let errorMessage = `Ошибка HTTP: ${response.status}`;
            
            try 
            {
                const errorData = JSON.parse( respText );
                if( errorData && errorData.message ) 
                {
                    errorMessage = errorData.message;
                }
            } 
            catch( error ) 
            {
                if( respText )
                {
                    errorMessage = respText;
                }
            }
            throw new Error( errorMessage );
        }

        const container = document.getElementById('table-container');
        if( container ) 
        {
            container.innerHTML = respText;
           
            const timeElements = document.querySelectorAll('.time');
            timeElements.forEach(element => {
                const elemTime = element.textContent.trim(); 
                
                if( elemTime ) 
                {
                    element.textContent = formatDate( elemTime );
                }
            });
        }
    } 
    catch( error ) 
    {
        console.error('Ошибка загрузки истории:', error);
    }
}

export function formatDate( dateString ) 
{
     const date = new Date( dateString );
     return new Intl.DateTimeFormat('ru-RU', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
     }).format(date);
}
