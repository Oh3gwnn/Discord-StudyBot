// npm install axios
import axios from 'axios';

export const handler = async(event) => {
    // TODO implement
    try {
        const payload = {
            content: "공부 시작 시간입니다.",
            embeds: [
                {
                    title: '📔 Let\'s Start Study!',
                    description: '50분 동안 열심히 공부합시다. 🔥',
                    color: 0xd3d3d3
                }
            ]
        };
        await axios.post('input webhook url', payload);

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