// npm install axios
import axios from 'axios';

export const handler = async(event) => {
    // TODO implement
    try {
        const result = await axios.post(
            'input webhook url', // WebHook URL
            {
          'content': '> ### 공부 시작시간입니다.\n'+'> 50분 동안 열심히 공부합시다 🔥'
        });
        console.info('웹훅 성공');
      } catch(error) {
        console.error('웹훅 실패', error);
      }
    
      const response = {
        statusCode: 200,
        body: JSON.stringify('Hello from Lambda!'),
      };
      return response;
    };